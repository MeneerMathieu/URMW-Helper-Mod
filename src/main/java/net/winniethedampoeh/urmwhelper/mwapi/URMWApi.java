package net.winniethedampoeh.urmwhelper.mwapi;

import net.winniethedampoeh.urmwhelper.URMWHelper;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.ToStringFunction;
import me.xdrop.fuzzywuzzy.model.BoundExtractedResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class URMWApi {

    /**
     *
     * @return a JSON object with the raw information from https://urmw.markng.me/api/info
     */
    public static @Nullable JSONObject getInfo(){
        JSONParser parse = new JSONParser();
        try {
            return (JSONObject) parse.parse(String.valueOf(getData("info")));
        }catch (Exception e){
            URMWHelper.LOGGER.info(e.toString());
            return null;
        }
    }

    /**
     *
     * @return a JSON object with the raw data from https://urmw.markng.me/api/standard-data
     */
    public static @Nullable JSONObject getStandardData(){
        JSONParser parse = new JSONParser();
        try {
            return (JSONObject) parse.parse(String.valueOf(getData("standard-data")));
        }catch (Exception e){
            URMWHelper.LOGGER.info(e.toString());
            return null;
        }
    }

    /**
     *
     * @return a JSON array with the raw data from https://urmw.markng.me/api/players
     */
    public static @Nullable JSONArray getPlayers(){
        JSONParser parse = new JSONParser();
        try {
            return (JSONArray) parse.parse(String.valueOf(getData("players")));
        }catch (Exception e){
            URMWHelper.LOGGER.info(e.toString());
            return null;
        }
    }

    /**
     *
     * @return a JSON array with the raw data from https://urmw.markng.me/api/achievements
     */
    public static @Nullable JSONArray getAchievements(){
        JSONParser parse = new JSONParser();
        try {
            return (JSONArray) parse.parse(String.valueOf(getData("achievements")));
        }catch (Exception e){
            URMWHelper.LOGGER.info(e.toString());
            return null;
        }
    }

    /**
     *
     * @return a JSON object with the raw data from https://urmw.markng.me/api/renames
     */
    public static @Nullable JSONObject getRenames(){
        JSONParser parse = new JSONParser();
        try {
            return (JSONObject) parse.parse(String.valueOf(getData("renames")));
        }catch (Exception e){
            URMWHelper.LOGGER.info(e.toString());
            return null;
        }
    }

    /**
     *
     * @param playerName playername
     * @return a JSON object with the raw data from https://urmw.markng.me/api/players, but only one player. Name has to be
     * an exact match.
     */
    public static @Nullable JSONObject getPlayerData(String playerName){
        JSONArray players = getPlayers();
        for (Object player : players) {
            JSONObject playerData = (JSONObject) player;
            String name = (String) playerData.get("name");
            if (Objects.equals(name, playerName)) {
                return playerData;
            }
        }
        return null;
    }


    /**
     *
     * @param name name of the achievement. Does not have to be an exact match.
     * @return a JSON object with the raw data from https://urmw.markng.me/api/achievements, but only one player.
     */
    public static JSONObject getAchievementData(String name) {
        JSONArray achievements = getAchievements();
        if (achievements == null) throw new AssertionError();
        String keyword = fuzzyMatch(Construct.listFromNestedJSONArray(achievements, "name"),name);
        for (Object achievement : achievements) {
            JSONObject achievementData = (JSONObject) achievement;
            String achievementName = (String) achievementData.get("name");
            if (Objects.equals(keyword, achievementName)) {
                return achievementData;
            }
        }
        return null;
    }
    /**
     *
     * @return a list of names of all URMW players.
     */
    public static List<String> getPlayerList(){
        JSONArray players = getPlayers();
        List<String> playerList = new ArrayList<>();
        for (Object player : players) {
            JSONObject playerData = (JSONObject) player;
            String name = (String) playerData.get("name");
            playerList.add(name);
        }
        return playerList;
    }

    /**
     *
     * @param name Your search term.
     * @return the correct player name that belongs with your search term.
     */
    public static @Nullable String searchPlayer(@NotNull String name){
        List<String> playerList = getPlayerList();
        if (playerList.size() == 0){
            return null;
        }
        return fuzzyMatch(playerList,name);
    }

    /**
     * Use this to search through lists of strings and find the closest match to your string.
     * @param list The list you want to search through
     * @param searchWord The keyword that will be searched.
     * @return The string that was the closest match.
     */
    public static @Nullable String fuzzyMatch(List<String> list, @NotNull String searchWord){
        BoundExtractedResult<String> match = FuzzySearch.extractOne(searchWord.toLowerCase(),
                list,
                (ToStringFunction<String>) String::toLowerCase);
        if(match.getScore() < 75){
            return null;
        }
        return match.getReferent();
    }

    /**
     *
     * @param count The amount of tournaments you want to fetch. 1 is the most recent. 2 are the two most recent tournaments.
     * @return a JSON array with the raw data from https://urmw.markng.me/api/tourneys/recent?count=${COUNT}
     */
    public static @Nullable JSONArray getRecentTourneys(int count){
        JSONParser parse = new JSONParser();
        String endpoint = "tourneys/recent?count=" + count;
        try {
            return (JSONArray) parse.parse(String.valueOf(getData(endpoint)));
        }catch (Exception e){
            URMWHelper.LOGGER.info(e.toString());
            return null;
        }
    }

    /**
     *
     * @param count The amount of matches you want to fetch. 1 is the most recent. 2 are the two most recent matches.
     * @return a JSON array with the raw data from https://urmw.markng.me/api/matches/recent?count=${COUNT}
     */
    public static @Nullable JSONArray getRecentMatches(int count){
        JSONParser parse = new JSONParser();
        String endpoint = "matches/recent?count=" + count;
        try {
            return (JSONArray) parse.parse(String.valueOf(getData(endpoint)));
        }catch (Exception e){
            URMWHelper.LOGGER.info(e.toString());
            return null;
        }
    }

    private static @Nullable StringBuilder getData(String endpoint){
        try {
            URL url = new URL("https://urmw.markng.me/api/" + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200){
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            }else {
                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }
                scanner.close();
                return informationString;
            }
        }catch(Exception e){
            URMWHelper.LOGGER.info(e.toString());
            return null;
        }
    }


}
