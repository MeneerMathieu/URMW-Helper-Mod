package net.winniethedampoeh.urmwhelper.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.util.Formatting;

public class TableFlipCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        dispatcher.register(ClientCommandManager.literal("tableflip")
                .executes(TableFlipCommand::run));
    }

    private static int run(CommandContext<FabricClientCommandSource> ctx) {
        ctx.getSource().getClient().player.sendChatMessage("(╯°□°）╯/︵ ┻━┻");
        return 1;
    }
}
