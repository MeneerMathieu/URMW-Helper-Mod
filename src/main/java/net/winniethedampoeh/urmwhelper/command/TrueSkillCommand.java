package net.winniethedampoeh.urmwhelper.command;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.winniethedampoeh.urmwhelper.parallelthread.TrueSkill;




public class TrueSkillCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        dispatcher.register(ClientCommandManager.literal("trueskill")
                .then(ClientCommandManager.argument("player", StringArgumentType.word())
                        .executes(TrueSkillCommand::run)));
        dispatcher.register(ClientCommandManager.literal("ts")
                .then(ClientCommandManager.argument("player",StringArgumentType.word())
                        .executes(TrueSkillCommand::run)));
    }



    private static int run(CommandContext<FabricClientCommandSource> context){
        Runnable r = new TrueSkill(context);
        new Thread(r).start();
        return 1;


    }
}
