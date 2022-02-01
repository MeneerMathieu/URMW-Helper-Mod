package net.winniethedampoeh.urmwhelper.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.winniethedampoeh.urmwhelper.parallelthread.MWUnlink;
import net.winniethedampoeh.urmwhelper.parallelthread.UpdatePlayers;


public class MWUnlinkCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        dispatcher.register(ClientCommandManager.literal("mwunlink")
                .then(ClientCommandManager.argument("mwplayer", StringArgumentType.word())
                    .executes(MWUnlinkCommand::run)));
    }

    private static int run(CommandContext<FabricClientCommandSource> ctx) {
        Runnable r = new MWUnlink(ctx);
        new Thread(r).start();

        Runnable l = new UpdatePlayers();
        new Thread(l).start();

        return 1;
    }
}
