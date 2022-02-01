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
    private final Float scale;
    private final Float height;
    private final boolean defaultRendering;

    public Config() throws FileNotFoundException, ParseException {
        loadConfig();
        JSONObject config = parseJSON();
        this.webAddress = (String) config.get("api-address");
        this.scale = Float.valueOf((String) config.get("scale"));
        this.height = Float.valueOf((String) config.get("height"));
        this.defaultRendering = (boolean) config.get("default-rendering");
    }

    public static void loadConfig(){
        try {
            File configFile = new File(filepath);
            new File(directoryPath).mkdirs();
            if(configFile.createNewFile()){
                FileWriter writer = new FileWriter(filepath);
                writer.write("""
                        {
                          "api-address": "https://urmw.markng.me/api/"
                          "scale": "0.50"
                          "height": "0.80"
                          "default-rendering": true
                        }""");
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
    public Float getScale(){
        return this.scale;
    }
    public Float getHeight(){
        return this.height;
    }
    public boolean getDefaultRendering(){
        return this.defaultRendering;
    }

}