package net.winniethedampoeh.urmwhelper.parallelthread;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.winniethedampoeh.urmwhelper.URMWHelper;
import net.winniethedampoeh.urmwhelper.config.Config;
import net.winniethedampoeh.urmwhelper.mwapi.MWPlayer;
import net.winniethedampoeh.urmwhelper.storage.UuidMap;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class MWLink implements Runnable{

    private final CommandContext<FabricClientCommandSource> ctx;
    private final String player;

    public MWLink(CommandContext<FabricClientCommandSource> ctx, String player){
        this.ctx = ctx;
        this.player = player;
    }

    @Override
    public void run() {
        String player = this.player;
        JSONParser parser = new JSONParser();
        JSONObject response = null;
        try {
            response = (JSONObject) parser.parse(String.valueOf(getData(player)));
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        String uuidString = response.get("id").toString();
        String part1 = uuidString.substring(0,8);
        String part2 = uuidString.substring(8,12);
        String part3 = uuidString.substring(12,16);
        String part4 = uuidString.substring(16,20);
        String part5 = uuidString.substring(20);
        String fullUUIDString = part1 + "-" + part2 + "-" + part3 + "-" + part4 + "-" + part5;
        UUID uuid = UUID.fromString(fullUUIDString);
       // UUID uuid = UUID.fromString(uuidString);

        MWPlayer mwPlayer = null;
        try {
            mwPlayer = new MWPlayer(StringArgumentType.getString(ctx, "mwplayer"));
        }catch (Exception e){
            ctx.getSource().sendFeedback(new LiteralText(Formatting.RED + "Missile wars player not found."));
            return;
        }
        UuidMap.makeMap();
        try {
            UuidMap.addMap(mwPlayer, uuid);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    private static @Nullable StringBuilder getData(String endpoint){
        try {
            URL url = new URL( "https://api.mojang.com/users/profiles/minecraft/" + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200){
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            }else {
                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }
                scanner.close();
                return informationString;
            }
        }catch(Exception e){
            URMWHelper.LOGGER.info(e.toString());
            return null;
        }
    }

}
