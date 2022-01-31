package net.winniethedampoeh.urmwhelper.command;

import com.mojang.brigadier.CommandDispatcher;


import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.winniethedampoeh.urmwhelper.URMWHelper;
import net.winniethedampoeh.urmwhelper.config.Config;
import net.winniethedampoeh.urmwhelper.parallelthread.MWLink;
import org.jetbrains.annotations.Nullable;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

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
        return 1;

    }

}
