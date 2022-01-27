package net.winniethedampoeh.urmwhelper.mwapi;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.Instant;
import java.util.List;

public class MWMatch {

    private final int id;
    private final List<MatchesPlayer> winners;
    private final List<MatchesPlayer> losers;
    private final Instant timestamp;

    /**
     * Creates a MWMatch object. You can either use sortType "recent" or "byID".
     * When using "recent", the constructor will find the Nth most recent match and return the data for it.
     * Example:
     *  MWMatch(1, recent) you will get the most recent match. With MWMatch(2, recent) you will get the second most recent Match.
     *
     * When using "byID", the constructor will find the match with the same id as "get".
     * Example:
     *  MWMatch(0, byID) will return the oldest match. MWMatch(99, byID) will return the 100th match.
     * @param get The ID or Nth most recent match.
     * @param sortType "byID" or "recent"
     */
    public MWMatch(int get, sortType sortType){
        JSONObject match = null;
        switch (sortType) {
            case BY_ID -> {
                int matchCount = new MWInfo().getMatchCount();
                int numberOfMatches = matchCount - get;
                match = (JSONObject) URMWApi.getRecentMatches(numberOfMatches).get(numberOfMatches - 1);
            }
            case RECENT -> match = (JSONObject) URMWApi.getRecentMatches(get).get(get - 1);
        }
        this.id = (int) (long) match.get("id");
        this.winners = Construct.matchesPlayers((JSONArray) match.get("winners"));
        this.losers = Construct.matchesPlayers((JSONArray) match.get("losers"));
        this.timestamp = Instant.parse((String) match.get("timestamp"));
    }

    public int getId(){
        return this.id;
    }

    public List<MatchesPlayer> getWinners(){
        return this.winners;
    }

    public List<MatchesPlayer> getLosers(){
        return this.losers;
    }

    public Instant getTimestamp(){
        return this.timestamp;
    }

}
