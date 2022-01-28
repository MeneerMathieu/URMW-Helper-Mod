package net.winniethedampoeh.urmwhelper.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.winniethedampoeh.urmwhelper.parallelthread.RankList;

public class RankListCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        dispatcher.register(ClientCommandManager.literal("ranklist")
                        .executes(RankListCommand::all)
                .then(ClientCommandManager.argument("from", IntegerArgumentType.integer(1))
                        .then(ClientCommandManager.argument("to", IntegerArgumentType.integer())
                                .executes(RankListCommand::run))));
    }

    private static int all(CommandContext<FabricClientCommandSource> ctx) {
        int from = 1;
        int to = 1000;
        Runnable r = new RankList(ctx, from, to);
        new Thread(r).start();
        return 1;
    }

    private static int run(CommandContext<FabricClientCommandSource> ctx) {
        int from = IntegerArgumentType.getInteger(ctx, "from");
        int to = IntegerArgumentType.getInteger(ctx, "to");
        Runnable r = new RankList(ctx, from, to);
        new Thread(r).start();
        return 1;
    }
}
