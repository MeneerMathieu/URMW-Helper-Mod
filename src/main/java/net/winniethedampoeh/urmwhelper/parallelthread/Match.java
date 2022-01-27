package net.winniethedampoeh.urmwhelper.parallelthread;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.winniethedampoeh.urmwhelper.URMWHelper;
import net.winniethedampoeh.urmwhelper.mwapi.*;

import java.util.List;

import static java.lang.Math.round;

public class Match implements Runnable{

    private final CommandContext<FabricClientCommandSource> ctx;
    private final net.winniethedampoeh.urmwhelper.mwapi.sortType sortType;
    private boolean mostRecent = false;

    public Match(CommandContext<FabricClientCommandSource> ctx, sortType sortType){
        this.ctx = ctx;
        this.sortType = sortType;
    }

    public Match(CommandContext<FabricClientCommandSource> ctx, sortType sortType, boolean mostRecent){
        this.ctx = ctx;
        this.sortType = sortType;
        this.mostRecent = true;
    }
    @Override
    public void run() {
        CommandContext<FabricClientCommandSource> ctx = this.ctx;
        sortType sortType = this.sortType;

        MWMatch match;
        try{
            if (this.mostRecent){
                match = new MWMatch(1, sortType);
            }
            else {
                match = new MWMatch(IntegerArgumentType.getInteger(ctx, "id"), sortType);
            }
        }catch (Exception e){
            ctx.getSource().sendFeedback(new LiteralText(Formatting.RED + "Tournament not found."));
            return;
        }
        List<MatchesPlayer> winners = match.getWinners();
        List<MatchesPlayer> losers = match.getLosers();
        int id = match.getId();


        String line0 = Formatting.DARK_GRAY + "Tourney stats:\n";
        String line1 = Formatting.GRAY + "  ID: " + Formatting.WHITE + id + "\n";
        String line2 = Formatting.GRAY + "  Loser:" + printPlayers(winners) + "\n";
        String line3 = Formatting.GRAY + "  Winner:" + printPlayers(losers);

        ctx.getSource().sendFeedback(new LiteralText(line0 + line1 + line2 + line3));
    }

    private String printPlayers(List<MatchesPlayer> playerList) {
        URMWHelper.LOGGER.info(playerList);
        String print = "";
        for (MatchesPlayer player : playerList){

            int ts = (int) round(player.getSkillBefore().getConservativeRating());
            int dv = (int) round(player.getSkillBefore().getStandardDeviation());
            int newTs = (int) round(player.getSkillAfter().getConservativeRating());
            int newDv = (int) round(player.getSkillAfter().getStandardDeviation());
            int dts = newTs - ts;
            int ddv = newDv - dv;
            String dTS, dDV;

            if (dts > -1) {
                dTS = "+" + dts;
            } else {
                dTS = String.valueOf(dts);
            }
            if (ddv > -1) {
                dDV = "+" + ddv;
            } else {
                dDV = String.valueOf(ddv);
            }
            print = print.concat("  " +Formatting.AQUA + player.getName() + ": " + Formatting.WHITE + "(" + dTS + ", " + dDV + ")  " + Formatting.GRAY + " (" + ts + ", " + dv + " -> " + newTs + ", " + newDv + ")\n");
        }
        return print;
    }
}
