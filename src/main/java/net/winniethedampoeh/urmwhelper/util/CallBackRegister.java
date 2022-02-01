package net.winniethedampoeh.urmwhelper.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.winniethedampoeh.urmwhelper.config.Rendering;

public class CallBackRegister {

    public static void registerCallBacks(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (KeyBindsRegister.toggleRendering.wasPressed()) {
                Rendering.toggleRendering();
            }
        });
    }
}
