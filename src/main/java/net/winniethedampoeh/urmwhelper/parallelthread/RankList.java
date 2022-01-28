package net.winniethedampoeh.urmwhelper.parallelthread;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.winniethedampoeh.urmwhelper.mwapi.URMWApi;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static java.lang.Math.round;

public class RankList implements Runnable{

    private final CommandContext<FabricClientCommandSource> ctx;

    public RankList(CommandContext<FabricClientCommandSource> ctx){
        this.ctx = ctx;
    }

    @Override
    public void run(){
        CommandContext<FabricClientCommandSource> ctx = this.ctx;
        JSONArray playersData;
        try {
            playersData = URMWApi.getPlayers();
        }catch (Exception e){
            ctx.getSource().sendFeedback(new LiteralText(Formatting.RED + "Something went wrong."));
            return;
        }

        int from = IntegerArgumentType.getInteger(ctx, "from");
        int to = IntegerArgumentType.getInteger(ctx, "to");
        assert playersData != null;
        String players = "";
        for (Object object : playersData){
            JSONObject player = (JSONObject) object;
            int rank = ((int) round((long) player.get("ranking")) + 1);
            if (rank >= from && rank <= to){
                String name = (String) player.get("name");
                players = players.concat(Formatting.GRAY + "  [" + rank + "] " + Formatting.DARK_AQUA + name + "\n");
            }
        }
        String line0 = Formatting.DARK_GRAY + "Rankings:\n \n";
        MutableText message = new LiteralText(line0 + players);
        ctx.getSource().sendFeedback(message);
    }
}
