package net.winniethedampoeh.urmwhelper.command;

import com.mojang.brigadier.CommandDispatcher;


import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.winniethedampoeh.urmwhelper.parallelthread.MWLink;
import net.winniethedampoeh.urmwhelper.parallelthread.UpdatePlayers;


public class MWLinkCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        dispatcher.register(ClientCommandManager.literal("mwlink")
                .then(ClientCommandManager.argument("player", EntityArgumentType.player())
                        .then(ClientCommandManager.argument("mwplayer", StringArgumentType.word())
                                .executes(MWLinkCommand::run))));
    }

    private static int run(CommandContext<FabricClientCommandSource> ctx) {
        String[] args = ctx.getInput().split(" ", -1);
        Runnable r = new MWLink(ctx, args[1]);
        new Thread(r).start();

        Runnable l = new UpdatePlayers();
        new Thread(l).start();

        return 1;

    }

}
