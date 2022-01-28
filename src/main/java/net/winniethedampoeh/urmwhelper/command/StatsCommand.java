package net.winniethedampoeh.urmwhelper.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.winniethedampoeh.urmwhelper.parallelthread.Stats;

public class StatsCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        dispatcher.register(ClientCommandManager.literal("stats")
                        .executes(StatsCommand::sender)
                .then(ClientCommandManager.argument("player", StringArgumentType.word())
                        .executes(StatsCommand::run)));
    }

    private static int run(CommandContext<FabricClientCommandSource> ctx) {
        Runnable r = new Stats(ctx, CommandTarget.STRING);
        new Thread(r).start();
        return 1;
    }

    private static int sender(CommandContext<FabricClientCommandSource> ctx) {
        Runnable r = new Stats(ctx, CommandTarget.SENDER);
        new Thread(r).start();
        return 1;
    }
}
