package net.winniethedampoeh.urmwhelper.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import de.gesundkrank.jskills.Rating;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.winniethedampoeh.urmwhelper.mwapi.Calculate;
import net.winniethedampoeh.urmwhelper.mwapi.MWPlayer;

import java.util.List;

import static java.lang.Math.round;

public class CalculateCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        dispatcher.register(ClientCommandManager.literal("calculate")
                .then(ClientCommandManager.argument("args", StringArgumentType.greedyString())
                        .executes(CalculateCommand::run)));
        dispatcher.register(ClientCommandManager.literal("calc")
                .then(ClientCommandManager.argument("args", StringArgumentType.greedyString())
                        .executes(CalculateCommand::run)));
    }


    private static int run(CommandContext<FabricClientCommandSource> ctx){
        MWPlayer player1, player2;
        String[] args;
        try{
            args = StringArgumentType.getString(ctx, "args").split(" ", -1);
        }catch (IllegalArgumentException ignore){
            args = new String[]{" "};
        }

        try{
            if (args.length == 1){
                player1 = new MWPlayer(ctx.getSource().getPlayer().getName().asString());
                player2 = new MWPlayer(args[0]);
            }else {
                player1 = new MWPlayer(args[0]);
                player2 = new MWPlayer(args[1]);
            }

            List<Rating> win1 = Calculate.calc(player1, player2);
            List<Rating> win2 = Calculate.calc(player2, player1);

            String part0 = Formatting.DARK_GRAY + "\n---  TrueSkill calculation:  ---\n \n";
            String part1 = makeCalcString(win1, player1, player2);
            String part2 = makeCalcString(win2, player2, player1);
            MutableText message = new LiteralText(part0 + part1 + part2);

            ctx.getSource().sendFeedback(message);
            return 1;
        }catch (Exception e){
            LiteralText error = new LiteralText(Formatting.RED + "Player not found");
            ctx.getSource().sendFeedback(error);
            System.out.println(e);
            return 1;
        }

    }

    private static String makeCalcString(List<Rating> ratings, MWPlayer winner, MWPlayer loser){
        String line0 = Formatting.WHITE + "If " + winner.getName() + " wins:\n";
        String line1 = makeLine(ratings, winner, 0);
        String line2 = makeLine(ratings, loser, 1);
        String line3 = " \n";

        return line0 + line1 + line2 + line3;
    }



    private static String makeLine(List<Rating> ratings, MWPlayer player, int index){
        int ts = (int) round(player.getSkill().getConservativeRating());
        int dv = (int) round(player.getSkill().getStandardDeviation());
        int newts = (int) round(((double)((ratings.get(index)).getConservativeRating())));
        int newdv = (int) round(((double)((ratings.get(index)).getStandardDeviation())));
        int dts = newts - ts;
        int ddv = newdv - dv;
        String dTS, dDV;

        if (dts > -1){
            dTS = "+" + dts;
        }else {
            dTS = String.valueOf(dts);
        }
        if (ddv > -1){
            dDV = "+" + ddv;
        }else{
            dDV = String.valueOf(ddv);
        }
        String line = Formatting.WHITE + player.getName() + ": " + Formatting.GRAY  + "(" + dTS + ", " + dDV + ")   ("+ ts + ", " + dv + " -> " + newts + ", " + newdv + ")\n";
        return line;
    }
}
