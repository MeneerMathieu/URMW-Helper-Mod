package net.winniethedampoeh.urmwhelper.util;

import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.winniethedampoeh.urmwhelper.command.*;

public class ModCommandRegister {
    public static void registerCommands() {
        AchievementCommand.register(ClientCommandManager.DISPATCHER);
        AchievementsCommand.register(ClientCommandManager.DISPATCHER);
        CalculateCommand.register(ClientCommandManager.DISPATCHER);
        StatsCommand.register(ClientCommandManager.DISPATCHER);
        TrueSkillCommand.register(ClientCommandManager.DISPATCHER);
        TournamentCommand.register(ClientCommandManager.DISPATCHER);
    }
}
