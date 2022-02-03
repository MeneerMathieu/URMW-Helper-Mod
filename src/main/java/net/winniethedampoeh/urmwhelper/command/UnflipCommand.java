package net.winniethedampoeh.urmwhelper.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

public class UnflipCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        dispatcher.register(ClientCommandManager.literal("unflip")
                .executes(UnflipCommand::run));
    }

    private static int run(CommandContext<FabricClientCommandSource> ctx) {
        ctx.getSource().getClient().player.sendChatMessage("┬─┬ ノ( ゜-゜ノ)");
        return 1;
    }
}
