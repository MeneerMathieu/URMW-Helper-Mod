package net.winniethedampoeh.urmwhelper.parallelthread;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.winniethedampoeh.urmwhelper.mwapi.MWPlayer;
import net.winniethedampoeh.urmwhelper.mwapi.URMWApi;
import net.winniethedampoeh.urmwhelper.storage.UuidMap;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.UUID;

public class MWUnlink implements Runnable{
    private final CommandContext<FabricClientCommandSource> ctx;

    public MWUnlink(CommandContext<FabricClientCommandSource> ctx){
        this.ctx = ctx;
    }

    public void run(){
        CommandContext<FabricClientCommandSource> ctx = this.ctx;
        MWPlayer mwPlayer = null;
        try {
            JSONArray playerData = URMWApi.getPlayers();
            mwPlayer = new MWPlayer(StringArgumentType.getString(ctx, "mwplayer"), playerData);
        }catch (Exception e){
            ctx.getSource().sendFeedback(new LiteralText(Formatting.RED + "Missile wars player not found."));
            return;
        }
        try {
            UuidMap.removeMap(mwPlayer);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return;
        }

        new UpdatePlayers().run();
        ctx.getSource().sendFeedback(new LiteralText(Formatting.GRAY + "Cleared the UUID mapping from " + mwPlayer.getName()));
    }
}
