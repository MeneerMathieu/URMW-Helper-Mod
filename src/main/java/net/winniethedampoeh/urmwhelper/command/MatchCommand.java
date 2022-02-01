package net.winniethedampoeh.urmwhelper.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.winniethedampoeh.urmwhelper.mwapi.sortType;
import net.winniethedampoeh.urmwhelper.parallelthread.Match;

public class MatchCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        dispatcher.register(ClientCommandManager.literal("match")
                .then(ClientCommandManager.literal("recent")
                        .executes(MatchCommand::mostRecent)
                        .then(ClientCommandManager.argument("id", IntegerArgumentType.integer())
                                .executes(MatchCommand::recent)))
                .then(ClientCommandManager.literal("id")
                        .then(ClientCommandManager.argument("id", IntegerArgumentType.integer())
                                .executes(MatchCommand::id))));
    }

    private static int mostRecent(CommandContext<FabricClientCommandSource> ctx) {
        Runnable r = new Match(ctx, sortType.RECENT, true);
        new Thread(r).start();
        return 1;
    }

    private static int id(CommandContext<FabricClientCommandSource> ctx) {
        Runnable r = new Match(ctx, sortType.BY_ID);
        new Thread(r).start();
        return 1;
    }

    private static int recent(CommandContext<FabricClientCommandSource> ctx) {
        Runnable r = new Match(ctx, sortType.RECENT);
        new Thread(r).start();
        return 1;
    }

}
