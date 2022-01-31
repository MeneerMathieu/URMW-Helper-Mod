package net.winniethedampoeh.urmwhelper.storage;

import net.winniethedampoeh.urmwhelper.URMWHelper;
import net.winniethedampoeh.urmwhelper.mwapi.URMWApi;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Players {
    private static final String filepath = "./URMW-Helper/players.json";
    private static final String directoryPath = "./URMW-Helper";
    private JSONArray players;

    public static void updatePlayers() throws IOException {
        JSONArray players = URMWApi.getPlayers();
        File playerFile = new File(filepath);
        new File(directoryPath).mkdirs();
        if (playerFile.createNewFile()){
            URMWHelper.LOGGER.info("New player storage made.");
        }
        FileWriter writer = new FileWriter(filepath);
        assert players != null;
        writer.write(players.toJSONString());
        writer.close();
    }

    public JSONArray getPlayers() throws ParseException, FileNotFoundException {
        String JSONString = "";
        File playerFile = new File(filepath);
        Scanner reader = new Scanner(playerFile);
        while (reader.hasNext()){
            JSONString = JSONString.concat(reader.nextLine());
        }
        reader.close();
        JSONParser parser = new JSONParser();
        this.players = (JSONArray) parser.parse(JSONString);
        return this.players;
    }

}
