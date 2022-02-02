package net.winniethedampoeh.urmwhelper.mwapi;

import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

public class MWAchievement {

    private final String name;
    private final String description;
    private final List<String> playersCompleted;

    /**
     *
     * @param name doesn't have to be an exact match for an achievement name. It will get searched.
     */
    public MWAchievement(String name){
        JSONObject achData = URMWApi.getAchievementData(name);
        this.name = (String) achData.get("name");
        this.description = (String) achData.get("description");
        this.playersCompleted = Construct.listFromJSONArray((JSONArray) achData.get("playersCompleted"));

    }

    public String getName(){
        return this.name;
    }

    public @Nullable String getDescription(){
        return this.description;
    }

    public List<String> getPlayersCompleted(){
        return this.playersCompleted;
    }
}
