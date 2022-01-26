package net.winniethedampoeh.urmwhelper.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.winniethedampoeh.urmwhelper.parallelthread.Calculate;


public class CalculateCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        dispatcher.register(ClientCommandManager.literal("calculate")
                .then(ClientCommandManager.argument("team 1", StringArgumentType.string())
                        .then(ClientCommandManager.argument("team 2", StringArgumentType.string())
                                .executes(CalculateCommand::run))));
        dispatcher.register(ClientCommandManager.literal("calc")
                .then(ClientCommandManager.argument("team 1", StringArgumentType.string())
                        .then(ClientCommandManager.argument("team 2", StringArgumentType.string())
                                .executes(CalculateCommand::run))));
    }


    private static int run(CommandContext<FabricClientCommandSource> ctx) {
        Runnable r = new Calculate(ctx);
        new Thread(r).start();
        return 1;
    }
}
