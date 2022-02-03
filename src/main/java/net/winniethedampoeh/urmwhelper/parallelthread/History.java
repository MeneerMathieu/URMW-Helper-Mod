package net.winniethedampoeh.urmwhelper.parallelthread;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.winniethedampoeh.urmwhelper.URMWHelper;
import net.winniethedampoeh.urmwhelper.mwapi.MWPlayer;
import net.winniethedampoeh.urmwhelper.mwapi.URMWApi;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.text.AttributeSet;
import javax.swing.text.Style;

public class History implements Runnable {

    private final CommandContext<FabricClientCommandSource> ctx;
    private final String player1;
    private final String player2;

    public History(CommandContext<FabricClientCommandSource> ctx, String player1, String player2) {
        this.ctx = ctx;
        this.player1 = player1;
        this.player2 = player2;
    }

    @Override
    public void run() {
        CommandContext<FabricClientCommandSource> ctx = this.ctx;
        String player1 = this.player1;
        String player2 = this.player2;
        JSONArray playersData = URMWApi.getPlayers();
        MWPlayer playerOne, playerTwo;
        try {
            playerOne = new MWPlayer(player1, playersData);
            playerTwo = new MWPlayer(player2, playersData);
        }catch (Exception e){
            ctx.getSource().sendFeedback(new LiteralText(Formatting.RED + "Player not found."));
            return;
        }


        int wins, losses;
        try {
            wins = playerOne.getWinsAgainst().get(playerTwo.getName());
        }catch (Exception e){
            wins = 0;
        }
        try {
            losses = playerOne.getLossesAgainst().get(playerTwo.getName());
        }catch (Exception e){
            losses = 0;
        }

        float winRate = (float) wins / (wins + losses);
        String winrate = String.format("%.0f", winRate * 100) + "%";

        Text message0 = new LiteralText("\n" + Formatting.AQUA + playerOne.getName() + Formatting.DARK_GRAY + "'s history against " + Formatting.AQUA + playerTwo.getName() + Formatting.DARK_GRAY + ":\n");
        Text message1 = new LiteralText(Formatting.GRAY + "  Wins: " + Formatting.WHITE + wins
                + Formatting.GRAY + "  Losses: " + Formatting.WHITE + losses
                + Formatting.GRAY + "  Win-rate: " + Formatting.WHITE + winrate + "\n");

        LiteralText message = new LiteralText(message0.asString() + message1.asString());
        ctx.getSource().sendFeedback(message);
    }
    
}