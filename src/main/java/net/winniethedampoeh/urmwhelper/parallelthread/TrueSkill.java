package net.winniethedampoeh.urmwhelper.parallelthread;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.winniethedampoeh.urmwhelper.mwapi.MWPlayer;

import java.util.Objects;

import static java.lang.Math.round;

public class TrueSkill implements Runnable{

    private final CommandContext<FabricClientCommandSource> ctx;

    public TrueSkill(CommandContext<FabricClientCommandSource> ctx){
        this.ctx = ctx;
    }

    @Override
    public void run(){
        CommandContext<FabricClientCommandSource> context = this.ctx;
        MWPlayer player;
        String arg;
        try{
            arg= StringArgumentType.getString(context, "player");
        }catch (IllegalArgumentException ignored){
            arg = " ";
        }
        try {
            if (Objects.equals(arg, " ")){
                player = new MWPlayer(context.getSource().getPlayer().getName().asString());
            }else {
                player = new MWPlayer(arg);
            }
        }catch (Exception e){
            LiteralText message = new LiteralText(Formatting.RED + "Player not found.");
            context.getSource().sendFeedback(message);
            return;
        }

        String urmwName = player.getName();
        int trueskill = (int) round(player.getSkill().getConservativeRating());
        int deviation = (int) round(player.getSkill().getStandardDeviation());

        String msg = Formatting.AQUA +urmwName +  Formatting.WHITE + "'s Trueskill: " + Formatting.GRAY + "(" + trueskill + ", " + deviation + ")";
        MutableText message = new LiteralText(msg).formatted(Formatting.RED);
        context.getSource().sendFeedback(message);
    }
}
