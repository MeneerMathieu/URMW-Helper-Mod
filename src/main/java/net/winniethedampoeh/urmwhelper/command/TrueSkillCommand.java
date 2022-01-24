package net.winniethedampoeh.urmwhelper.command;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.winniethedampoeh.urmwhelper.mwapi.MWPlayer;

import java.util.Objects;

import static java.lang.Math.round;


public class TrueSkillCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        dispatcher.register(ClientCommandManager.literal("trueskill")
                .then(ClientCommandManager.argument("arg", StringArgumentType.word())
                        .executes(TrueSkillCommand::run)));
        dispatcher.register(ClientCommandManager.literal("ts")
                .then(ClientCommandManager.argument("arg",StringArgumentType.word())
                        .executes(TrueSkillCommand::run)));
    }



    private static int run(CommandContext<FabricClientCommandSource> context){
        MWPlayer player;
        String arg;
        try{
            arg= StringArgumentType.getString(context, "arg");
        }catch (IllegalArgumentException ignored){
            arg = new String(" ");
        }
        if (Objects.equals(arg, " ")){
            player = new MWPlayer(context.getSource().getPlayer().getName().asString());
        }else {
            player = new MWPlayer(arg);
        }

        if (player.getSkill() == null){
            context.getSource().sendError(new LiteralText("Player not found."));
            return 0;
        }
        String urmwName = player.getName();
        int trueskill = (int) round(player.getSkill().getConservativeRating());
        int deviation = (int) round(player.getSkill().getStandardDeviation());

        String msg = Formatting.WHITE +urmwName + "'s Trueskill: " + Formatting.GRAY + "(" + trueskill + ", " + deviation + ")";
        MutableText message = new LiteralText(msg).formatted(Formatting.RED);
        context.getSource().sendFeedback(message);
        return 1;


    }
}
