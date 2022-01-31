package net.winniethedampoeh.urmwhelper.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.winniethedampoeh.urmwhelper.URMWHelper;
import net.winniethedampoeh.urmwhelper.parallelthread.MWLink;
import net.winniethedampoeh.urmwhelper.parallelthread.MWUnlink;

import java.util.List;
import java.util.UUID;

public class MWUnlinkCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        dispatcher.register(ClientCommandManager.literal("mwunlink")
                .then(ClientCommandManager.argument("mwplayer", StringArgumentType.word())
                    .executes(MWUnlinkCommand::run)));
    }

    private static int run(CommandContext<FabricClientCommandSource> ctx) {
        Runnable r = new MWUnlink(ctx);
        new Thread(r).start();
        return 1;
    }
}
