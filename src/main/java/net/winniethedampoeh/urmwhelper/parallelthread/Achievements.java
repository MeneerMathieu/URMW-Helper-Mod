package net.winniethedampoeh.urmwhelper.parallelthread;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.winniethedampoeh.urmwhelper.mwapi.MWPlayer;
import net.winniethedampoeh.urmwhelper.mwapi.URMWApi;
import org.json.simple.JSONArray;

import java.util.List;

public class Achievements implements Runnable{
    private final CommandContext<FabricClientCommandSource> ctx;

    public Achievements(CommandContext<FabricClientCommandSource> ctx){
        this.ctx = ctx;
    }

    @Override
    public void run(){
        CommandContext<FabricClientCommandSource> ctx = this.ctx;
        MWPlayer player;
        JSONArray playerdata = URMWApi.getPlayers();
        try{
            player = new MWPlayer(StringArgumentType.getString(ctx,"player"), playerdata);
        }catch (Exception e){
            ctx.getSource().sendFeedback(new LiteralText(Formatting.RED + "Player not found."));
            return;
        }
        String string = generateString(player.getName(), player.getCompletedAchievements());
        MutableText message = new LiteralText(string);
        ctx.getSource().sendFeedback(message);
    }

    private String generateString(String name, List<String> completedAchievements) {
        String string = Formatting.AQUA + name + Formatting.DARK_GRAY + "'s completed achievements:\n" + Formatting.DARK_AQUA;
        for (String completedAchievement : completedAchievements) {
            string = string.concat(" -  " + completedAchievement + "\n");
        }
        return string;
    }
}
