package net.winniethedampoeh.urmwhelper.mwapi;

import de.gesundkrank.jskills.GameInfo;
import org.json.simple.JSONObject;

import java.time.Instant;

public class MWInfo {
    private final Instant lastUpdated;
    private final int playerCount;
    private final int matchCount;
    private final int tourneyCount;
    private final int achievementCount;
    private final GameInfo trueSkillSettings;

    public MWInfo(){
        JSONObject info = URMWApi.getInfo();
        this.lastUpdated = Instant.parse((String) info.get("lastUpdated"));
        this.playerCount = (int) (long) info.get("playerCount");
        this.matchCount = (int) (long) info.get("matchCount");
        this.tourneyCount = (int) (long) info.get("tourneyCount");
        this.achievementCount = (int) (long) info.get("achievementCount");
        this.trueSkillSettings = Construct.gameInfo((JSONObject) info.get("trueskillSettings"));
    }

    public Instant getLastUpdated(){
        return this.lastUpdated;
    }

    public int getPlayerCount(){
        return this.playerCount;
    }

    public int getMatchCount(){
        return this.matchCount;
    }

    public int getTourneyCount(){
        return this.tourneyCount;
    }

    public int getAchievementCount(){
        return this.achievementCount;
    }

    public GameInfo getTrueSkillSettings(){
        return this.trueSkillSettings;
    }
}
