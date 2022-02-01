package net.winniethedampoeh.urmwhelper.parallelthread;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import de.gesundkrank.jskills.Rating;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.winniethedampoeh.urmwhelper.URMWHelper;
import net.winniethedampoeh.urmwhelper.mwapi.MWPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Math.round;

public class Calculate implements Runnable{

    private final CommandContext<FabricClientCommandSource> ctx;

    public Calculate(CommandContext<FabricClientCommandSource> ctx){
        this.ctx = ctx;
    }

    @Override
    public void run() {
        CommandContext<FabricClientCommandSource> ctx = this.ctx;
        List<MWPlayer> team1 = new ArrayList<>();
        List<MWPlayer> team2 = new ArrayList<>();
        String[] players1;
        try{
            players1 = StringArgumentType.getString(ctx, "team 1").split("\\+", -1);
        }catch (IllegalArgumentException ignore){
            players1 = new String[]{" "};
        }
        String[] players2;
        try{
            players2 = StringArgumentType.getString(ctx, "team 2").split("\\+", -1);
        }catch (IllegalArgumentException ignore){
            players2 = new String[]{" "};
        }

        try {
            for (String s : players1) {
                MWPlayer player = new MWPlayer(s);
                team1.add(player);
            }
            for (String s : players2) {
                MWPlayer player = new MWPlayer(s);
                team2.add(player);
            }
        }catch (Exception e){
            ctx.getSource().sendFeedback(new LiteralText(Formatting.RED + "Player not found."));
            return;
        }

        List<Map<MWPlayer, Rating>> win1 = net.winniethedampoeh.urmwhelper.mwapi.Calculate.calc(team1, team2);
        List<Map<MWPlayer, Rating>> win2 = net.winniethedampoeh.urmwhelper.mwapi.Calculate.calc(team2, team1);

        String part0 = Formatting.DARK_GRAY + "---  TrueSkill calculation:  ---\n";
        String part01 = Formatting.DARK_AQUA + "Team 1:  " + Formatting.GRAY + printTeam(team1) + "\n";
        String part02 = Formatting.DARK_AQUA + "Team 2:  " + Formatting.GRAY + printTeam(team2) + "\n\n";
        String part1 = Formatting.AQUA + "If team 1 wins:\n" +
                makeCalcString(win1) + "\n";
        String part2 = Formatting.AQUA + "If team 2 wins:\n" +
                makeCalcString(win2);
        MutableText message = new LiteralText(part0 + part01 + part02 + part1 + part2);

        ctx.getSource().sendFeedback(message);


    }

    private static String makeCalcString(List<Map<MWPlayer, Rating>> ratings){
        String line1 = makeLine(ratings, 0);
        String line2 = makeLine(ratings, 1);


        return line1 + line2;
    }


    private static String printTeam(List<MWPlayer> team){
        String print ="";
        for (MWPlayer player : team){
            print = print.concat(player.getName()) + " ";
        }
        return print;
    }

    private static String makeLine(List<Map<MWPlayer, Rating>> ratings, int index){
        String line = "";
        Map<MWPlayer, Rating> ratingMap = ratings.get(index);
        //URMWHelper.LOGGER.info(ratingMap);
        for (MWPlayer player : ratingMap.keySet()) {
            Rating rating = ratingMap.get(player);

            int ts = (int) round(player.getSkill().getConservativeRating());
            int dv = (int) round(player.getSkill().getStandardDeviation());
            int newTs = (int) round((double) rating.getConservativeRating());
            int newDv = (int) round((double) rating.getStandardDeviation());
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
            line = line.concat("  " + Formatting.DARK_AQUA + player.getName() + ": " + Formatting.WHITE + "(" + dTS + ", " + dDV + ")  " + Formatting.GRAY + " (" + ts + ", " + dv + " -> " + newTs + ", " + newDv + ")\n");
        }
        return line;
    }
}


