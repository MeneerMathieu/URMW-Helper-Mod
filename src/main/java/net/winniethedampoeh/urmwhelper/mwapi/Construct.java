package net.winniethedampoeh.urmwhelper.mwapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.gesundkrank.jskills.GameInfo;
import de.gesundkrank.jskills.Rating;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        String jsonString = list.toJSONString();
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            Map<String, Integer> map = objectMapper.readValue(jsonString, Map.class);
            return map;
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    protected static List<String> listFromJSONArray(JSONArray array){
        List<String> list = new ArrayList<>();
        for(int i = 0; i < array.size() -1; i++){
            list.add(i, (String) array.get(i));
        }
        return list;
    }

    protected static @NotNull List<MWPlayer> playerListFromJSONArray(@NotNull JSONArray array){
        List<MWPlayer> list = new ArrayList<>();
        for (int i = 0; i < array.size() - 1; i++){
            MWPlayer player = new MWPlayer((String) array.get(i));
            list.add(i, player);
        }
        return list;
    }

    protected static List<MatchesPlayer> matchesPlayers(JSONArray players){
        List<MatchesPlayer> list = new ArrayList<>();
        for (int i = 0; i < players.size() - 1; i++){
            MatchesPlayer player = new MatchesPlayer((JSONObject) players.get(i));
            list.add(i, player);
        }
        return list;
    }

}
