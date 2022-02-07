package net.winniethedampoeh.urmwhelper.parallelthread;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.winniethedampoeh.urmwhelper.command.util.Location;
import net.winniethedampoeh.urmwhelper.command.util.StringUtil;
import net.winniethedampoeh.urmwhelper.mwapi.MWPlayer;
import net.winniethedampoeh.urmwhelper.mwapi.URMWApi;

import java.sql.Array;
import java.util.HashMap;
import java.util.Map;

public class SingleHistory implements Runnable{

    private final CommandContext<FabricClientCommandSource> ctx;
    private final String player;

    public SingleHistory(CommandContext<FabricClientCommandSource> ctx, String player){
        this.ctx = ctx;
        this.player = player;
    }

    @Override
    public void run() {
        CommandContext<FabricClientCommandSource> ctx = this.ctx;
        String player = this.player;
        MWPlayer player1;
        try {
            player1 = new MWPlayer(player, URMWApi.getPlayers());
        } catch (Exception e) {
            ctx.getSource().sendFeedback(new LiteralText(Formatting.RED + "Player not found."));
            return;
        }
        Map<String, Integer[]> history = makeBetter(player1.getLossesAgainst(), player1.getWinsAgainst());


        String part1 = Formatting.AQUA + player1.getName() + Formatting.DARK_GRAY + "'s history:";
        String part2 = buildHistoryString(history);

        ctx.getSource().sendFeedback(new LiteralText(part1 + part2));
    }

    private String buildHistoryString(Map<String, Integer[]> history) {
        String historyString = "";
        for (String playerName : history.keySet()){
            int wins = history.get(playerName)[0];
            int losses = history.get(playerName)[1];
            float winrate = (float) wins/(wins + losses);
            historyString = historyString.concat("\n  " + Formatting.DARK_AQUA + StringUtil.gridBuilder(playerName + ":", 17, Location.AFTER) +
                    Formatting.GRAY + "  Wins: " + Formatting.WHITE + wins +
                    Formatting.GRAY + "  Losses: " + Formatting.WHITE + losses +
                    Formatting.GRAY + "  Winrate: " + Formatting.WHITE + String.format("%.0f", winrate * 100) + "%");
        }
        return historyString;
    }

    private Map<String, Integer[]> makeBetter(Map<String, Integer> losses, Map<String, Integer> wins){
        Map<String, Integer[]> better = new HashMap<>();
        for (String playerName : losses.keySet()){
            better.put(playerName, new Integer[]{0, losses.get(playerName)});
        }
        for (String playerName: wins.keySet()){
            if (better.getOrDefault(playerName, null) == null ){
                better.put(playerName, new Integer[]{wins.get(playerName), 0});
            }else {
                Integer winsAgainst = better.get(playerName)[1];
                better.replace(playerName, new Integer[]{wins.get(playerName), winsAgainst});
            }
        }
        return better;
    }
}
