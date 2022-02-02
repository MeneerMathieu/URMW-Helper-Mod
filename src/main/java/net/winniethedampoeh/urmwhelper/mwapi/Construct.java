package net.winniethedampoeh.urmwhelper.mwapi;

;
import de.gesundkrank.jskills.GameInfo;
import de.gesundkrank.jskills.Rating;
import net.winniethedampoeh.urmwhelper.URMWHelper;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

import static java.lang.Double.parseDouble;

public class Construct {

    /**
     *Constructs a Rating from a JSON skill object.
     *
     * @param skill JSONObject. Needs to look like this:
     *              {
     *              "mean": 1234,
     *              "deviation": 12,
     *              "trueskill": 1198
     *               }
     */
    protected static @NotNull Rating rating(@NotNull JSONObject skill){
        double mean = parseDouble(skill.get("mean").toString());
        double standardDeviation = parseDouble(skill.get("deviation").toString());
        return new Rating(mean, standardDeviation);
    }

    protected static @NotNull GameInfo gameInfo(JSONObject trueSkillSettings){

        double initialMean = parseDouble(trueSkillSettings.get("mu").toString());
        double initialStandardDeviation = parseDouble(trueSkillSettings.get("sigma").toString());
        double beta = parseDouble(trueSkillSettings.get("beta").toString());
        double dynamicFactor = parseDouble(trueSkillSettings.get("tau").toString());
        double drawProbability = parseDouble(trueSkillSettings.get("drawProbability").toString());

        return new GameInfo(initialMean, initialStandardDeviation, beta, dynamicFactor, drawProbability);
    }


    protected static Map<String, Integer> listAgainst(JSONObject list){
        Map<String, Integer> map = new HashMap<>();
        Iterator<String> iterator = list.keySet().iterator();;
        while (iterator.hasNext()){
            String next = iterator.next();
            int i = (int) (long) list.get(next);
            map.put(next, i);
        }
        URMWHelper.LOGGER.info(map);
        return map;
    }

    protected static List<String> listFromJSONArray(JSONArray array){
        List<String> list = new ArrayList<>();
        for(int i = 0; i < array.size(); i++){
            list.add(i, (String) array.get(i));
        }
        return list;
    }

    protected static List<String> listFromNestedJSONArray(JSONArray array, String name){
        List<String> list = new ArrayList<>();
        for(int i = 0; i < array.size(); i++){
            list.add(i, (String) ((JSONObject) array.get(i)).get(name));
        }
        return list;
    }

    protected static @NotNull List<MWPlayer> playerListFromJSONArray(@NotNull JSONArray array){
        List<MWPlayer> list = new ArrayList<>();
        for (int i = 0; i < array.size(); i++){
            MWPlayer player = new MWPlayer((String) array.get(i));
            list.add(i, player);
        }
        return list;
    }

    protected static List<MatchesPlayer> matchesPlayers(JSONArray players){
        List<MatchesPlayer> list = new ArrayList<>();
        for (int i = 0; i < players.size(); i++){
            MatchesPlayer player = new MatchesPlayer((JSONObject) players.get(i));
            list.add(i, player);
        }
        return list;
    }

}
