package net.winniethedampoeh.urmwhelper.mwapi;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.Instant;
import java.util.List;

public class MWTournament {
    private final int id;
    private final List<MWPlayer> first;
    private final List<MWPlayer> second;
    private final List<MWPlayer> third;
    private final Instant timestamp;

    /**
     * Creates a MWTournament object. You can either use sortType "recent" or "byID".
     * When using "recent", the constructor will find the Nth most recent tournament and return the data for it.
     * Example:
     *  MWTournament(1, recent) you will get the most recent tournament. With MWTournament(2, recent) you will get the second most recent tournament.
     *
     * When using "byID", the constructor will find the tournament with the same id as "get".
     * Example:
     *  MWTournament(0, byID) will return the oldest tournament. MWTournament(99, byID) will return the 100th tournament.
     * @param get The ID or Nth most recent match.
     * @param sortType "byID" or "recent"
     */
    public MWTournament(int get, sortType sortType) {
        JSONObject tourney = null;
        switch (sortType) {
            case byID:
                int tourneyCount = new MWInfo().getTourneyCount();
                int numberOfTourneys = tourneyCount - get;
                tourney = (JSONObject) URMWApi.getRecentTourneys(numberOfTourneys).get(numberOfTourneys - 1);
                break;
            case recent:
                tourney = (JSONObject) URMWApi.getRecentTourneys(get).get(get-1);
                break;
        }
        this.id = (int) (long) tourney.get("id");
        this.first = Construct.playerListFromJSONArray((JSONArray) tourney.get("first"));
        this.second = Construct.playerListFromJSONArray((JSONArray) tourney.get("second"));
        this.third = Construct.playerListFromJSONArray((JSONArray) tourney.get("third"));
        this.timestamp = Instant.parse((String) tourney.get("timestamp"));
    }

    public int getId(){
        return this.id;
    }

    public List<MWPlayer> getFirst(){
        return this.first;
    }

    public List<MWPlayer> getSecond(){
        return this.second;
    }

    public List<MWPlayer> getThird(){
        return this.third;
    }

    public Instant getTimestamp(){
        return this.timestamp;
    }



}
