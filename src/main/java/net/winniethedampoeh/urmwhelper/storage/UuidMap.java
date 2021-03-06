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
import java.util.*;

public class UuidMap {

    private static final String filepath = "./URMW-Helper/uuidmap.json";
    private static final String directoryPath = "./URMW-Helper";

    public static boolean makeMap(){
        try {
            File mapFile = new File(filepath);
            new File(directoryPath).mkdirs();
            if(mapFile.createNewFile()){
                FileWriter writer = new FileWriter(filepath);
                writer.write("{\"Fury_101\":\"76cbcec0-52d7-40dd-9d8c-c5e0541868af\",\"UnWin\":\"898e3c11-b8de-452a-8f01-e69aa920c6c1\",\"Ranger\":\"bbfb724e-13b8-4f13-8a5a-a553946ec9e9\",\"Knifehand87\":\"1dc4a5cf-e3a3-454b-97e7-626f91979da7\",\"Prozan\":\"65d9f91d-85cd-441f-aa8d-637d799577ec\",\"Smurfii\":\"65fdb272-1ae7-4005-ba8b-0ddb27d8aa87\",\"eragaurd\":\"6300895e-799f-42d6-ad2a-f74fd0554740\",\"Craft49er\":\"d4c9d484-29bd-4f90-bbf1-073fe5e87e1f\",\"Malino05\":\"941aa9fc-4c61-466b-a13b-d5968ec157fd\",\"Oggabob\":\"c1966b05-c928-480e-8ccb-c91bb2dbdea0\",\"ArvidTT\":\"3804c9a7-4118-4806-86a7-9ab925249083\",\"WinnieTheDampoeh\":\"c38cc1a5-c6d5-43f5-a8b0-924ca100deff\",\"SplashBrotherBE\":\"e3e3dbc6-a34d-45f8-86f6-dc3d8a06ba38\",\"builder993\":\"69d97bf1-27d7-4286-9245-cb93b27d7395\",\"Cistic\":\"772bd601-51d8-4367-8898-af79536ac92c\",\"EpicPix\":\"b3cf4790-3e7e-43bd-a1e4-d03df1253f18\",\"DSK1273\":\"cdb767f7-1481-4d42-89f5-725d7568160f\",\"Logic\":\"1118da9b-30c3-4144-979c-59f984f36977\",\"Canonic\":\"5580d489-7580-4617-b08f-d50ea7dca7e7\",\"Zorua\":\"350e8cc3-5792-4deb-8b81-03e5bf2041c6\",\"BombLW\":\"19cc6705-9f0e-4dfe-8c46-d7cf855371b2\",\"VAJvagyok\":\"f48bc8e3-2fa7-4ce3-92a1-9e47bb0e7d1d\",\"Eon\":\"30bb0ab6-49dd-4494-8fd7-af0e09fcf083\",\"Eevee342585\":\"aa0c365d-8669-4601-a974-5ebc7fb9e72a\",\"CAG\":\"0bcafcd2-05d4-4afc-9c9f-f38467a3847b\",\"TurkeyLauncher\":\"06a2d084-9827-4d19-ba4a-6456f3418237\",\"TheAI\":\"26757a96-fad2-4d68-b28f-5de2d6f2e7fe\",\"extended_piston4\":\"f6a94f41-2d22-4db3-b677-6301c934775f\",\"Jelly_exe\":\"dbb48b5c-588c-43e9-bc91-d8802063192d\",\"SaintKlaus\":\"de977230-616c-4dab-9e93-73985aac2656\",\"Squibs\":\"313f3d6a-0985-4d6e-b2fc-e442c40006e5\",\"porcus8\":\"90ae56ab-9b2e-4863-9ba7-628746466708\",\"Drakan\":\"c66277a9-b934-4ea0-b84a-2b91027f81cd\",\"owakolf\":\"caf0ba7f-97a0-4493-ba5e-d41b61a1ba0a\",\"Kurusa\":\"f98f0f72-4bd7-44b5-bec3-6b1fde8d81d3\"}");
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
        //URMWHelper.LOGGER.info(mappings);
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
        //URMWHelper.LOGGER.info(parser.parse(JSONString));
        return (JSONObject) parser.parse(JSONString);
    }

    public static Map<UUID, String> getMap() throws FileNotFoundException, ParseException {
        JSONObject jsonMap = loadMap();
        Map<UUID, String> uuidMap = new HashMap<>();
        for (Object o : jsonMap.keySet()) {
            String playerName = (String) o;
            UUID uuid = UUID.fromString((String) jsonMap.get(playerName));
            uuidMap.put(uuid, playerName);
        }
        //URMWHelper.LOGGER.info(uuidMap);
        return uuidMap;
    }
}
