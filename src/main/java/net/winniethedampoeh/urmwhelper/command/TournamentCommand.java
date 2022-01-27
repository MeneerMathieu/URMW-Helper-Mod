package net.winniethedampoeh.urmwhelper.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.winniethedampoeh.urmwhelper.mwapi.sortType;
import net.winniethedampoeh.urmwhelper.parallelthread.Tournament;

public class TournamentCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        dispatcher.register(ClientCommandManager.literal("tournament")
                .then(ClientCommandManager.literal("recent")
                        .then(ClientCommandManager.argument("id", IntegerArgumentType.integer())
                                .executes(TournamentCommand::recent)))
                .then(ClientCommandManager.literal("id")
                        .then(ClientCommandManager.argument("id", IntegerArgumentType.integer())
                                .executes(TournamentCommand::id))));
        dispatcher.register(ClientCommandManager.literal("tourney")
                .then(ClientCommandManager.literal("recent")
                        .executes(TournamentCommand::mostRecent)
                        .then(ClientCommandManager.argument("id", IntegerArgumentType.integer())
                                .executes(TournamentCommand::recent)))
                .then(ClientCommandManager.literal("id")
                        .then(ClientCommandManager.argument("id", IntegerArgumentType.integer())
                                .executes(TournamentCommand::id))));
    }

    private static int mostRecent(CommandContext<FabricClientCommandSource> ctx) {
        Runnable r = new Tournament(ctx, sortType.RECENT, true);
        new Thread(r).start();
        return 1;
    }

    private static int id(CommandContext<FabricClientCommandSource> ctx) {
        Runnable r = new Tournament(ctx, sortType.BY_ID);
        new Thread(r).start();
        return 1;
    }

    private static int recent(CommandContext<FabricClientCommandSource> ctx) {
        Runnable r = new Tournament(ctx, sortType.RECENT);
        new Thread(r).start();
        return 1;
    }
}
