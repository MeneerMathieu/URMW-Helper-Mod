package net.winniethedampoeh.urmwhelper.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.winniethedampoeh.urmwhelper.parallelthread.Achievements;

public class AchievementsCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        dispatcher.register(ClientCommandManager.literal("achievements")
                .then(ClientCommandManager.argument("player", StringArgumentType.word())
                        .executes(AchievementsCommand::run)));
    }

    private static int run(CommandContext<FabricClientCommandSource> ctx){
        Runnable r = new Achievements(ctx);
        new Thread(r).start();
        return 1;
    }
}
