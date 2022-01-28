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
                .then(ClientCommandManager.argument("from", IntegerArgumentType.integer(1))
                        .then(ClientCommandManager.argument("to", IntegerArgumentType.integer())
                                .executes(RankListCommand::run))));
    }

    private static int run(CommandContext<FabricClientCommandSource> ctx) {
        Runnable r = new RankList(ctx);
        new Thread(r).start();
        return 1;
    }
}
