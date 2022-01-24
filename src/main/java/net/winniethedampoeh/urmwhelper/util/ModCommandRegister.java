package net.winniethedampoeh.urmwhelper.util;

import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.winniethedampoeh.urmwhelper.command.CalculateCommand;
import net.winniethedampoeh.urmwhelper.command.TrueSkillCommand;

public class ModCommandRegister {
    public static void registerCommands() {
        TrueSkillCommand.register(ClientCommandManager.DISPATCHER);
        CalculateCommand.register(ClientCommandManager.DISPATCHER);
    }
}
