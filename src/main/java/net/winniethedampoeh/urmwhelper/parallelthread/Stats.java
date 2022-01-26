package net.winniethedampoeh.urmwhelper.parallelthread;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import de.gesundkrank.jskills.Rating;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.winniethedampoeh.urmwhelper.mwapi.MWPlayer;

public class Stats implements Runnable{
    private final CommandContext<FabricClientCommandSource> ctx;

    public Stats(CommandContext<FabricClientCommandSource> ctx){
        this.ctx = ctx;
    }

    @Override
    public void run(){
        CommandContext<FabricClientCommandSource> ctx = this.ctx;
        MWPlayer player;
        try{
            player = new MWPlayer(StringArgumentType.getString(ctx,"player"));
        }catch (Exception e){
            String message = Formatting.RED + "Player not found.";
            ctx.getSource().sendFeedback(new LiteralText(message));
            return;
        }

        String name = player.getName();
        Rating skill = player.getSkill();
        Rating peakSkill = player.getPeakSkill();
        int ranking = player.getRanking();
        String rankName = player.getRankName();
        int wins = player.getWins();
        int losses = player.getLosses();
        int streak = player.getStreak();
        int placedFirst = player.getTimesPlacedFirst();
        int placedSecond = player.getTimesPlacedSecond();
        int placedThird = player.getTimesPlacedThird();
        int completedAchievements = player.getCompletedAchievements().size();

        String line0 = Formatting.AQUA + name + "'s stats:\n";
        String line1 = Formatting.GRAY + "  Skill: " + Formatting.WHITE + "(" + skill.getConservativeRating() + ", " + skill.getStandardDeviation() + ")"
                + Formatting.GRAY + "  Peak skill: " + Formatting.WHITE + "(" + peakSkill.getConservativeRating() + ", " + peakSkill.getStandardDeviation() + ")\n";
        String line2 = Formatting.GRAY + "  Ranking: " + Formatting.WHITE + "(" + rankName + ", " + ranking + ")"
                + Formatting.GRAY + "  Completed achievements: " + Formatting.WHITE + completedAchievements + "\n";
        String line3 = Formatting.GRAY + "  Matches won: " + Formatting.WHITE + wins
                + Formatting.GRAY + "    Matches lost: " + Formatting.WHITE + losses + "\n";
        String line4 = Formatting.GRAY + "  Streak: " + Formatting.WHITE + streak + "\n";
        String line5 = Formatting.GRAY + "  Tourneys:\n";
        String line6 = Formatting.GRAY + "  1st: " + Formatting.WHITE + placedFirst + Formatting.GRAY + "  2nd: " + Formatting.WHITE
                + placedSecond + Formatting.GRAY + "  3rd: " + Formatting.WHITE + placedThird;
        String message = line0 + line1 + line2 + line3 + line4 + line5 + line6;
        ctx.getSource().sendFeedback(new LiteralText(message));
    }
}
