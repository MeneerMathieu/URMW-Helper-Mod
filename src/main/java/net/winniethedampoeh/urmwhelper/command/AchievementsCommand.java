package net.winniethedampoeh.urmwhelper.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;
import net.winniethedampoeh.urmwhelper.parallelthread.Achievements;
import net.winniethedampoeh.urmwhelper.parallelthread.AchievementsList;

public class AchievementsCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        dispatcher.register(ClientCommandManager.literal("achievements")
                        .executes(AchievementsCommand::list)
                .then(ClientCommandManager.argument("player", StringArgumentType.word())
                        .executes(AchievementsCommand::run)));
        dispatcher.register(ClientCommandManager.literal("achs")
                        .executes(AchievementsCommand::list)
                .then(ClientCommandManager.argument("player", StringArgumentType.word())
                        .executes(AchievementsCommand::run)));
    }

    private static int list(CommandContext<FabricClientCommandSource> ctx) {
        Runnable r = new AchievementsList(ctx);
        new Thread(r).start();
        return 1;
    }

    private static int run(CommandContext<FabricClientCommandSource> ctx){
        Runnable r = new Achievements(ctx);
        new Thread(r).start();
        return 1;
    }
}
