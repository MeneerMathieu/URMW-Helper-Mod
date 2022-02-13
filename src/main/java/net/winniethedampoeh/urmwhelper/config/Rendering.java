package net.winniethedampoeh.urmwhelper.config;

import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.winniethedampoeh.urmwhelper.URMWHelper;

public class Rendering {
    public static boolean doRendering = URMWHelper.getInstance().getConfig().getDefaultRendering();
    public static boolean doRenderColor = URMWHelper.getInstance().getConfig().doRenderColor();

    public static void toggleRendering(){
        doRendering = !doRendering;
        URMWHelper.minecraftClient.player.sendMessage(new LiteralText(Formatting.GREEN + "Toggled name tag rendering, now " + doRendering), true);
    }

    public static void toggleRenderColor(){
        doRenderColor = !doRenderColor;
        URMWHelper.minecraftClient.player.sendMessage(new LiteralText(Formatting.GREEN + "Toggled color rendering, now "  + doRenderColor),true);
    }

}
