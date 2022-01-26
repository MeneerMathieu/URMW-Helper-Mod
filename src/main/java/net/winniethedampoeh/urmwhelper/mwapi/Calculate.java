package net.winniethedampoeh.urmwhelper.mwapi;

import de.gesundkrank.jskills.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Calculate {


    /**
    *Use calc to calculate the new score of players. It returns JSON Array giving the new trueskill and deviation if team 1 wins.
    * @param winner Winner of the game.
    * @param loser Loser of the game.
    * @return a new List containing Map of MWPlayers and ratings. The 0th item are the winners, the 1st item are the losers.
     */
    public static @NotNull List<Map<MWPlayer, Rating>> calc(@NotNull List<MWPlayer> winner, @NotNull List<MWPlayer> loser){
        GameInfo gameInfo = new MWInfo().getTrueSkillSettings();
        Team winners = new Team();
        Team losers = new Team();
        for (MWPlayer mwPlayer : winner) {
            Player<MWPlayer> player = new Player<>(mwPlayer);
            winners.addPlayer(player, mwPlayer.getSkill());
        }
        for (MWPlayer mwPlayer : loser){
            Player<MWPlayer> player = new Player<>(mwPlayer);
            losers.addPlayer(player, mwPlayer.getSkill());
        }

        int winnerRank = 1;
        int loserRank = 2;

        Map<IPlayer, Rating> calculation = TrueSkillCalculator.calculateNewRatings(gameInfo,Team.concat(winners,losers), winnerRank, loserRank);

        Map<MWPlayer, Rating> winnerRatings = new HashMap<>();
        for (MWPlayer mwPlayer : winner){
            Player<MWPlayer> player = new Player<>(mwPlayer);
            Rating r = calculation.get(player);
            winnerRatings.put(mwPlayer, r);
        }
        Map<MWPlayer, Rating> loserRatings = new HashMap<>();
        for (MWPlayer mwPlayer: loser){
            Player<MWPlayer> player = new Player<>(mwPlayer);
            Rating r = calculation.get(player);
            winnerRatings.put(mwPlayer, r);
        }
        List<Map<MWPlayer, Rating>> calc = new ArrayList<>();
        calc.add(winnerRatings);
        calc.add(loserRatings);

        return calc;

    }

}
