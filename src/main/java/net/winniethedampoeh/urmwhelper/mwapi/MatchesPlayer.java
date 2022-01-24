package net.winniethedampoeh.urmwhelper.mwapi;

import de.gesundkrank.jskills.Rating;
import org.json.simple.JSONObject;

public class MatchesPlayer {
    private final String name;
    private final Rating skillBefore;
    private final Rating skillAfter;

    public MatchesPlayer(JSONObject json){
        this.name = (String) json.get("name");
        this.skillBefore = Construct.rating((JSONObject) json.get("skillBefore"));
        this.skillAfter = Construct.rating((JSONObject) json.get("skillAfter"));
    }

    public String getName(){
        return this.name;
    }

    public Rating getSkillBefore(){
        return this.skillBefore;
    }

    public Rating getSkillAfter(){
        return this.skillAfter;
    }
}
