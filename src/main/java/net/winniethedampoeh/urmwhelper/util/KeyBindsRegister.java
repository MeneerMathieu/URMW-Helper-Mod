package net.winniethedampoeh.urmwhelper.util;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindsRegister {

    public static KeyBinding toggleRendering;
    public static KeyBinding toggleColorRendering;

    public static void registerKeyBinds(){
        toggleRendering = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.urmwhelper.toggle-render",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_EQUAL,
                "category.urmwhelper.all"
        ));
        toggleColorRendering = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.urmwhelper.toggle-color-render",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_MINUS,
                "category.urmwhelper.all"
        ));
    }
}
