package net.winniethedampoeh.urmwhelper.storage;

import net.winniethedampoeh.urmwhelper.URMWHelper;
import net.winniethedampoeh.urmwhelper.mwapi.MWPlayer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;

public class UuidMap {

    private static final String filepath = "./URMW-Helper/uuidmap.json";
    private static final String directoryPath = "./URMW-Helper";

    public static boolean makeMap(){
        try {
            File mapFile = new File(filepath);
            new File(directoryPath).mkdirs();
            if(mapFile.createNewFile()){
                FileWriter writer = new FileWriter(filepath);
                writer.write("{}");
                writer.close();
                URMWHelper.LOGGER.info("New UUID Map file made.");
                return true;
            }else {
                URMWHelper.LOGGER.info("UUID Mapping file was already there.");
                return false;
            }
        }catch (IOException e){
            URMWHelper.LOGGER.info("Something went wrong while creating a UUID mapping file.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addMap(MWPlayer player, UUID uuid) throws IOException, ParseException {
        JSONObject mappings = loadMap();
        mappings.put(player.getName(), uuid.toString());
        URMWHelper.LOGGER.info(mappings);
        writeMap(mappings);
        return true;
    }

    public static boolean removeMap(MWPlayer player) throws IOException, ParseException {
        JSONObject mappings = loadMap();
        mappings.remove(player.getName());
        writeMap(mappings);
        return true;
    }

    private static void writeMap(JSONObject mappings) throws IOException {
        FileWriter writer = new FileWriter(filepath);
        writer.write(mappings.toJSONString());
        writer.close();
    }

    public static JSONObject loadMap() throws ParseException, FileNotFoundException {
        String JSONString = "";
        File mapFile = new File(filepath);
        Scanner reader = new Scanner(mapFile);
        while (reader.hasNext()){
            JSONString = JSONString.concat(reader.next());
        }
        reader.close();
        JSONParser parser = new JSONParser();
        URMWHelper.LOGGER.info(parser.parse(JSONString));
        return (JSONObject) parser.parse(JSONString);
    }
}
