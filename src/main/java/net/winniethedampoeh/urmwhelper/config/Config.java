package net.winniethedampoeh.urmwhelper.config;

import net.winniethedampoeh.urmwhelper.URMWHelper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Config {
    private static final String filepath = "./config/urmw-helper.config.json";
    private static final String directoryPath= "./config";

    private final String webAddress;

    public Config() throws FileNotFoundException, ParseException {
        loadConfig();
        JSONObject config = parseJSON();
        this.webAddress = (String) config.get("api-address");
    }

    public static void loadConfig(){
        try {
            File configFile = new File(filepath);
            new File(directoryPath).mkdirs();
            if(configFile.createNewFile()){
                FileWriter writer = new FileWriter(filepath);
                writer.write("{\n" +
                        "  \"api-address\": \"https://urmw.markng.me/api/\"\n" +
                        "}");
                writer.close();
                URMWHelper.LOGGER.info("New config file made");
            }else {
                URMWHelper.LOGGER.info("Config loaded.");
            }
        }catch (IOException e){
            URMWHelper.LOGGER.info("Something went wrong while creating a config file.");
            e.printStackTrace();
        }
    }

    private JSONObject parseJSON() throws FileNotFoundException, ParseException {
        String JSONString = "";
        File configFile = new File(filepath);
        Scanner reader = new Scanner(configFile);
        while (reader.hasNext()){
            JSONString = JSONString.concat(reader.next());
        }
        reader.close();
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(JSONString);
    }

    public String getWebAddress(){
        return this.webAddress;
    }
}
