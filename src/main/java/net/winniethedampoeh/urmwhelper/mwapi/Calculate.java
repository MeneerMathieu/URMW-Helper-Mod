package net.winniethedampoeh.urmwhelper.mwapi;

import de.gesundkrank.jskills.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Calculate {


    /**
    *Use calc to calculate the new score of players. It returns JSON Array giving the new trueskill and deviation if team 1 wins.
    * @param winner Winner of the game.
    * @param loser Loser of the game.
    * @return a new List containing a list of 2 Rating objects.
     */
    public static @NotNull List<Rating> calc(@NotNull MWPlayer winner, @NotNull MWPlayer loser){
        GameInfo gameInfo = new MWInfo().getTrueSkillSettings();
        Player<String> p1 = new Player<>("p1");
        Player<String> p2 = new Player<>("p2");
        Team team1 = new Team(p1,winner.getSkill());
        Team team2 = new Team(p2,loser.getSkill());
        int team1rank = 1;
        int team2rank = 2;

        Map<IPlayer, Rating> calculation = TrueSkillCalculator.calculateNewRatings(gameInfo,Team.concat(team1,team2), team1rank, team2rank);

        Rating r1 = calculation.get(p1);
        Rating r2 = calculation.get(p2);
        List<Rating> calc= new ArrayList<>();
        calc.add(0, r1);
        calc.add(1, r2);

        return calc;

    }

}
