package net.winniethedampoeh.urmwhelper.parallelthread;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import de.gesundkrank.jskills.Rating;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.winniethedampoeh.urmwhelper.command.CommandTarget;
import net.winniethedampoeh.urmwhelper.mwapi.MWPlayer;
import net.winniethedampoeh.urmwhelper.mwapi.URMWApi;
import org.json.simple.JSONArray;

import java.util.Locale;

import static java.lang.Math.round;

public class Stats implements Runnable{
    private final CommandContext<FabricClientCommandSource> ctx;
    private final CommandTarget target;

    public Stats(CommandContext<FabricClientCommandSource> ctx, CommandTarget target){
        this.ctx = ctx;
        this.target = target;
    }

    @Override
    public void run(){
        CommandContext<FabricClientCommandSource> ctx = this.ctx;
        CommandTarget target = this.target;
        MWPlayer player = null;
        JSONArray players = URMWApi.getPlayers();
        try{
            switch (target){
                case SENDER -> player = new MWPlayer(ctx.getSource().getPlayer().getName().asString(), players);
                case STRING -> player = new MWPlayer(StringArgumentType.getString(ctx, "player"), players);
            }
        }catch (Exception e){
            String message = Formatting.RED + "Player not found.";
            ctx.getSource().sendFeedback(new LiteralText(message));
            return;
        }

        String name = player.getName();
        Rating skill = player.getSkill();
        Rating peakSkill = player.getPeakSkill();
        int ranking = player.getRanking() + 1;
        String rankName = player.getRankName();
        String RankName = rankName.substring(0,1).toUpperCase(Locale.ENGLISH) + rankName.substring(1);
        int wins = player.getWins();
        int losses = player.getLosses();
        int streak = player.getStreak();
        float winRatio = player.getWinRatio();
        int placedFirst = player.getTimesPlacedFirst();
        int placedSecond = player.getTimesPlacedSecond();
        int placedThird = player.getTimesPlacedThird();
        int completedAchievements = player.getCompletedAchievements().size();

        String line0 = Formatting.AQUA + name + "'s stats:\n";
        String line1 = Formatting.GRAY + "  Skill: " + Formatting.WHITE + "(" + (int) round(skill.getConservativeRating()) + ", " + (int) round(skill.getStandardDeviation()) + ")"
                + Formatting.GRAY + "  Peak skill: " + Formatting.WHITE + "(" + (int) round(peakSkill.getConservativeRating()) + ", " + (int) round(peakSkill.getStandardDeviation()) + ")\n";
        String line2 = Formatting.GRAY + "  Ranking: " + Formatting.WHITE + "(" + RankName + ", " + ranking + ")"
                + Formatting.GRAY + "  Completed achievements: " + Formatting.WHITE + completedAchievements + "\n";
        String line3 = Formatting.GRAY + "  Matches won: " + Formatting.WHITE + wins
                + Formatting.GRAY + "    Matches lost: " + Formatting.WHITE + losses + "\n";
        String line4 = Formatting.GRAY + "  Streak: " + Formatting.WHITE + streak + Formatting.GRAY + "  Win ratio: " + Formatting.WHITE + String.format("%.0f",winRatio * 100) + "%" + "\n";
        String line5 = Formatting.GRAY + "  Tourneys:\n";
        String line6 = Formatting.GRAY + "  1st: " + Formatting.WHITE + placedFirst + Formatting.GRAY + "  2nd: " + Formatting.WHITE
                + placedSecond + Formatting.GRAY + "  3rd: " + Formatting.WHITE + placedThird;
        String message = line0 + line1 + line2 + line3 + line4 + line5 + line6;
        ctx.getSource().sendFeedback(new LiteralText(message));
    }
}
