package net.winniethedampoeh.urmwhelper.mwapi;

import de.gesundkrank.jskills.Rating;
import net.winniethedampoeh.urmwhelper.storage.Players;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class MWPlayer {


    private final String name;
    private final Rating skill;
    private final Rating peakSkill;
    private final int ranking;
    private final int wins;
    private final int losses;
    private final float winRatio;
    private final int timesPlacedFirst;
    private final int timesPlacedSecond;
    private final int timesPlacedThird;
    private final Map<String, Integer> winsAgainst;
    private final Map<String, Integer> lossesAgainst;
    private final int streak;
    private final List<String> completedAchievements;
    private final String rankName;

    /**
     * Constructs a new MWPlayer object. Gets all the data from the URMW API.
     * @param playerName doesn't have to be the exact player name. Can also be something else. For example: "winnie" generates MWPlayer WinnieTheDampoeh
     */
    public MWPlayer(String playerName, JSONArray playersData){
        JSONObject playerData = URMWApi.getPlayerData(URMWApi.searchPlayer(playerName, playersData), playersData);
        this.name = (String) playerData.get("name");
        this.skill = Construct.rating((JSONObject) playerData.get("skill"));
        this.peakSkill = Construct.rating((JSONObject) playerData.get("peakSkill"));
        this.ranking = Math.toIntExact((long) playerData.get("ranking"));
        this.wins = Math.toIntExact((long) playerData.get("wins"));
        this.losses = Math.toIntExact((long) playerData.get("losses"));
        this.winRatio = (float) this.wins/(this.wins + this.losses);
        this.timesPlacedFirst =  Math.toIntExact((long) playerData.get("timesPlacedFirst"));
        this.timesPlacedSecond =  Math.toIntExact((long) playerData.get("timesPlacedSecond"));
        this.timesPlacedThird =  Math.toIntExact((long) playerData.get("timesPlacedThird"));
        this.winsAgainst = Construct.listAgainst((JSONObject) playerData.get("winsAgainst"));
        this.lossesAgainst = Construct.listAgainst((JSONObject) playerData.get("lossesAgainst"));
        this.streak = Math.toIntExact((long) playerData.get("streak"));
        this.completedAchievements = Construct.listFromJSONArray((JSONArray) playerData.get("completedAchievements"));
        this.rankName = (String) playerData.get("rankName");
    }

    public MWPlayer(String playerName, boolean fromFile) throws NullPointerException, FileNotFoundException, ParseException {
        if (!fromFile) throw new NullPointerException();
        JSONArray players = new Players().getPlayers();
        for (Object player : players){
            JSONObject playerData = (JSONObject) player;
            if (playerData.get("name").equals(playerName)){
                this.name = (String) playerData.get("name");
                this.skill = Construct.rating((JSONObject) playerData.get("skill"));
                this.peakSkill = Construct.rating((JSONObject) playerData.get("peakSkill"));
                this.ranking = Math.toIntExact((long) playerData.get("ranking"));
                this.wins = Math.toIntExact((long) playerData.get("wins"));
                this.losses = Math.toIntExact((long) playerData.get("losses"));
                this.winRatio = (float) this.wins/(this.wins + this.losses);
                this.timesPlacedFirst =  Math.toIntExact((long) playerData.get("timesPlacedFirst"));
                this.timesPlacedSecond =  Math.toIntExact((long) playerData.get("timesPlacedSecond"));
                this.timesPlacedThird =  Math.toIntExact((long) playerData.get("timesPlacedThird"));
                this.winsAgainst = Construct.listAgainst((JSONObject) playerData.get("winsAgainst"));
                this.lossesAgainst = Construct.listAgainst((JSONObject) playerData.get("lossesAgainst"));
                this.streak = Math.toIntExact((long) playerData.get("streak"));
                this.completedAchievements = Construct.listFromJSONArray((JSONArray) playerData.get("completedAchievements"));
                this.rankName = (String) playerData.get("rankName");
                System.out.println(rankName);
                return;
            }
        }
        throw new NullPointerException();
    }

    /**
     *@return the playername of a MWPlayer object.
     */
    public String getName(){
        return this.name;
    }

    /**
     * @return the rating of a MWPlayer object.
     */
    public Rating getSkill(){
        return this.skill;
    }


    /**
     * @return the peak rating of a MWPlayer object.
     */
    public Rating getPeakSkill(){
        return this.peakSkill;
    }

    /**
     * @return the ranking in URMW of a MWPlayer object. Ranking 0 means first place.
     */
    public int getRanking(){
        return this.ranking;
    }

    /**
     * @return the total wins of a MWPlayer object.
     */
    public int getWins(){
        return this.wins;
    }

    /**
     *  @return the total losses of a MWPlayer object.
     */
    public int getLosses(){
        return this.losses;
    }

    public float getWinRatio(){
        return this.winRatio;
    }

    public int getTimesPlacedFirst(){
        return this.timesPlacedFirst;
    }

    public int getTimesPlacedSecond(){
        return this.timesPlacedSecond;
    }

    public int getTimesPlacedThird(){
        return this.timesPlacedThird;
    }


    public Map<String, Integer> getWinsAgainst(){
        return this.winsAgainst;
    }

    public Map<String,Integer> getLossesAgainst(){
        return this.lossesAgainst;
    }
    /**
     *
     * @return the streak of a MWPlayer object.
     */
    public int getStreak(){
        return this.streak;
    }

    public List<String> getCompletedAchievements(){return this.completedAchievements;}

    /**
     *
     * @return the rank name of a MWPlayer object.
     */
    public String getRankName(){
        return this.rankName;
    }


}
