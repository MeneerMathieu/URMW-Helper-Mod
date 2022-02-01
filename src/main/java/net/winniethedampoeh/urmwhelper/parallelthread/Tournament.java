package net.winniethedampoeh.urmwhelper.parallelthread;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.winniethedampoeh.urmwhelper.URMWHelper;
import net.winniethedampoeh.urmwhelper.mwapi.MWPlayer;
import net.winniethedampoeh.urmwhelper.mwapi.MWTournament;
import net.winniethedampoeh.urmwhelper.mwapi.sortType;

import java.util.List;

public class Tournament implements Runnable{

    private final CommandContext<FabricClientCommandSource> ctx;
    private final sortType sortType;
    private boolean mostRecent = false;

    public Tournament(CommandContext<FabricClientCommandSource> ctx, sortType sortType){
        this.ctx = ctx;
        this.sortType = sortType;
    }

    public Tournament(CommandContext<FabricClientCommandSource> ctx, sortType sortType, boolean mostRecent){
        this.ctx = ctx;
        this.sortType = sortType;
        this.mostRecent = true;
    }
    @Override
    public void run() {
        CommandContext<FabricClientCommandSource> ctx = this.ctx;
        sortType sortType = this.sortType;

        MWTournament tourney;
        try{
            if (this.mostRecent){
                tourney = new MWTournament(1, sortType);
            }
            else {
                tourney = new MWTournament(IntegerArgumentType.getInteger(ctx, "id"), sortType);
            }
        }catch (Exception e){
            ctx.getSource().sendFeedback(new LiteralText(Formatting.RED + "Tournament not found."));
            return;
        }
        List<MWPlayer> first = tourney.getFirst();
        List<MWPlayer> second = tourney.getSecond();
        List<MWPlayer> third = tourney.getThird();
        int id = tourney.getId();

        String line0 = Formatting.DARK_GRAY + "Tourney stats:\n";
        String line1 = Formatting.GRAY + "ID: " + Formatting.WHITE + id + "\n";
        String line2 = Formatting.GRAY + "  First place: " + Formatting.AQUA + printPlayers(first) + "\n";
        String line3 = Formatting.GRAY + "  Second place: " + Formatting.DARK_AQUA + printPlayers(second) + "\n";
        String line4 = Formatting.GRAY + "  Third place: " + Formatting.DARK_AQUA + printPlayers(third);

        ctx.getSource().sendFeedback(new LiteralText(line0 + line1 + line2 + line3 + line4));
    }

    private String printPlayers(List<MWPlayer> playerList) {
        //URMWHelper.LOGGER.info(playerList);
        String print = "";
        for (MWPlayer player : playerList){
            print = print.concat(player.getName() + " ");
        }
        return print;
    }
}
