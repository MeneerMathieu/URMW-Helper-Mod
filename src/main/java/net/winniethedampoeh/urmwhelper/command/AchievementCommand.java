package net.winniethedampoeh.urmwhelper.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.winniethedampoeh.urmwhelper.parallelthread.Achievement;

public class AchievementCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        dispatcher.register(ClientCommandManager.literal("achievement")
                .then(ClientCommandManager.argument("achievement", StringArgumentType.greedyString())
                        .executes(AchievementCommand::run)));
        dispatcher.register(ClientCommandManager.literal("ach")
                .then(ClientCommandManager.argument("achievement", StringArgumentType.greedyString())
                        .executes(AchievementCommand::run)));
    }

    private static int run(CommandContext<FabricClientCommandSource> ctx) {
        Runnable r = new Achievement(ctx);
        new Thread(r).start();
        return 1;
    }
}
