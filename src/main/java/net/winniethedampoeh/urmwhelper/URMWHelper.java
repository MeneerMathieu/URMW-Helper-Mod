package net.winniethedampoeh.urmwhelper;

import net.fabricmc.api.ModInitializer;
import net.winniethedampoeh.urmwhelper.config.Config;
import net.winniethedampoeh.urmwhelper.storage.Players;
import net.winniethedampoeh.urmwhelper.util.ModCommandRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class URMWHelper implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LogManager.getLogger("urmwhelper");

    private static URMWHelper INSTANCE;
    private Config config;

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        INSTANCE = this;
        try {
            this.config = new Config();
            Players.updatePlayers();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        ModCommandRegister.registerCommands();
        LOGGER.info("URWM-Helper loaded.");
    }

    public static URMWHelper getInstance(){
        return INSTANCE;
    }

    public Config getConfig(){
        return config;
    }
}
