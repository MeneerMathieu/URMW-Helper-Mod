package net.winniethedampoeh.urmwhelper.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.winniethedampoeh.urmwhelper.URMWHelper;
import net.winniethedampoeh.urmwhelper.parallelthread.History;

public class HistoryCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        dispatcher.register(ClientCommandManager.literal("history")
                .then(ClientCommandManager.argument("player one", StringArgumentType.word())
                        .executes(context -> run(context, context.getSource().getPlayer().getName().asString(), StringArgumentType.getString(context, "player one")))
                        .then(ClientCommandManager.argument("player two", StringArgumentType.word())
                                .executes(context -> run(context, StringArgumentType.getString(context, "player one"), StringArgumentType.getString(context, "player two"))))));
    }

    private static int run(CommandContext<FabricClientCommandSource> ctx, String player_one, String player_two) {
        Runnable r = new History(ctx, player_one, player_two);
        new Thread(r).start();
        return 1;
    }
}
