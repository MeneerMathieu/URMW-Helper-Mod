package net.winniethedampoeh.urmwhelper.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Direction;
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

    private static final String defaultConfig = """
                        {
                          "api-address": "https://urmw.markng.me/api/",
                          "space-between-labels": "0.265",
                          "below-name": true,
                          "default-rendering": true,
                          "render-in-color": true
                        }""";

    private String webAddress;
    private Float spaceBetween;
    private boolean belowName;
    private boolean defaultRendering;
    private boolean renderColor;

    public Config() throws IOException, ParseException {
        loadConfig();
        JSONObject config;

        try {
            config = parseJSON();
            this.webAddress = (String) config.get("api-address");
            this.spaceBetween = Float.valueOf((String) config.get("space-between-labels"));
            this.belowName = (boolean) config.get("below-name");
            this.defaultRendering = (boolean) config.get("default-rendering");
            this.renderColor = (boolean) config.get("render-in-color");
        }catch (Exception e){
            resetConfig();
            config = parseJSON();
            this.webAddress = (String) config.get("api-address");
            this.spaceBetween = Float.valueOf((String) config.get("space-between-labels"));
            this.belowName = (boolean) config.get("below-name");
            this.defaultRendering = (boolean) config.get("default-rendering");
            this.renderColor = (boolean) config.get("render-in-color");
        }


    }

    public static void loadConfig(){
        try {
            File configFile = new File(filepath);
            new File(directoryPath).mkdirs();
            if(configFile.createNewFile()){
                FileWriter writer = new FileWriter(filepath);
                writer.write(defaultConfig);
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

    private static void resetConfig() throws IOException {
        FileWriter writer = new FileWriter(filepath);
        writer.write(defaultConfig);
        writer.close();
        URMWHelper.LOGGER.warn("Config was not in the right format and has been reset.");
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
    public Float getSpaceBetween(){
        return this.spaceBetween;
    }
    public boolean isBelowName(){
        return this.belowName;
    }
    public boolean getDefaultRendering(){
        return this.defaultRendering;
    }
    public boolean doRenderColor(){
        return this.renderColor;
    }



}
