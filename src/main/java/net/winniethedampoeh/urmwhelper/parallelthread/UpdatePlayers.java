package net.winniethedampoeh.urmwhelper.parallelthread;

import net.winniethedampoeh.urmwhelper.URMWHelper;
import net.winniethedampoeh.urmwhelper.storage.Players;
import net.winniethedampoeh.urmwhelper.storage.UuidMap;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class UpdatePlayers implements Runnable{

    @Override
    public void run() {
        try {
            Players.updatePlayers();
            URMWHelper.getInstance().setUUIDMap(UuidMap.getMap());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
