package net.winniethedampoeh.urmwhelper.parallelthread;


import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.winniethedampoeh.urmwhelper.mwapi.URMWApi;

import java.util.List;

public class AchievementsList implements Runnable{
    private final CommandContext<FabricClientCommandSource> ctx;

    public AchievementsList(CommandContext<FabricClientCommandSource> ctx){
        this.ctx = ctx;
    }

    public void run(){
        List<String> achievementList = URMWApi.getAchievementList();
        String line0 = Formatting.AQUA + "URMW " + Formatting.DARK_GRAY + "achievements:\n";
        String line1 = makeString(achievementList);

        MutableText message = new LiteralText(line0 + line1);
        ctx.getSource().sendFeedback(message);

    }

    private String makeString(List<String> achievementList) {
        String string = "";
        for (String achievement : achievementList) {
            string = string.concat(
                    Formatting.DARK_AQUA + "  - " + achievement + "\n"
            );
        }
        return string;
    }
}
