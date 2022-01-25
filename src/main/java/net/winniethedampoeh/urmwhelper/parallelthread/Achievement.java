package net.winniethedampoeh.urmwhelper.parallelthread;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.winniethedampoeh.urmwhelper.URMWHelper;
import net.winniethedampoeh.urmwhelper.mwapi.MWAchievement;
import net.winniethedampoeh.urmwhelper.mwapi.MWPlayer;

import java.util.List;

public class Achievement implements Runnable {
    private final CommandContext<FabricClientCommandSource> ctx;

    public Achievement(CommandContext<FabricClientCommandSource> ctx){
        this.ctx =ctx;
    }

    @Override
    public void run() {
        CommandContext<FabricClientCommandSource> ctx = this.ctx;
        MWAchievement achievement;
        try{
            achievement = new MWAchievement(StringArgumentType.getString(ctx, "name"));
        }catch (Exception e){
            ctx.getSource().sendFeedback(new LiteralText(Formatting.RED + "Achievement not found."));
            URMWHelper.LOGGER.info(e.getLocalizedMessage());
            return;
        }
        String name = achievement.getName();
        String description = achievement.getDescription();
        List<MWPlayer> playersCompleted = achievement.getPlayersCompleted();

        String line0 = Formatting.DARK_GRAY + "Achievement: " + Formatting.AQUA  + name + "\n";
        String line1 = Formatting.GRAY + "  " + description + "\n";
        String line2 = Formatting.DARK_AQUA + "  " + generateString(playersCompleted);
        ctx.getSource().sendFeedback(new LiteralText(line0 + line1 +line2));
    }

    private String generateString(List<MWPlayer> playersCompleted) {
        String output = "";
        try {
            for (int i = 0; i < playersCompleted.size(); i++){
                if (i == 0){
                    output = output.concat(playersCompleted.get(i).getName());
                }else {
                    output = output.concat(", " + playersCompleted.get(i).getName());
                }
            }
        }catch (Exception e){
            URMWHelper.LOGGER.info(e.getLocalizedMessage());
            output = Formatting.GRAY + "Not completed yet. Will you be the first?";
        }
        return output;
    }
}
