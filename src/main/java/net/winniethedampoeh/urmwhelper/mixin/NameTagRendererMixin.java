package net.winniethedampoeh.urmwhelper.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Matrix4f;
import net.winniethedampoeh.urmwhelper.URMWHelper;
import net.winniethedampoeh.urmwhelper.config.Rendering;
import net.winniethedampoeh.urmwhelper.mwapi.MWPlayer;
import net.winniethedampoeh.urmwhelper.parallelthread.UpdatePlayers;
import org.json.simple.parser.ParseException;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.lang.Math.round;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public class NameTagRendererMixin<T extends Entity> {

    protected EntityRenderDispatcher dispatcher;

    @Final
    private TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

    private static final float SPACE_BETWEEN = URMWHelper.getInstance().getConfig().getSpaceBetween();
    private static final boolean BELOW_NAME = URMWHelper.getInstance().getConfig().isBelowName();

    private static int coolDown = 0;
    private static Map<UUID, String> uuidMap = URMWHelper.getInstance().UUIDMap;
    private static Map<UUID, MWPlayer> mwPlayerMap = new HashMap<>();

    @Inject(cancellable = true, method = "renderLabelIfPresent(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "HEAD"))
    public void renderLabelIfPresent(AbstractClientPlayerEntity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) throws IOException, ParseException {
        dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        double d = dispatcher.getSquaredDistanceToCamera(entity);
        if (d > 4069.0) return;
        boolean bl = !entity.isSneaky();
        int i = "deadmau5".equals(text.getString()) ? -10 : 0;

        uuidMap = URMWHelper.getInstance().UUIDMap;
        if (doMWRendering(entity)){
            float fl = entity.getHeight() + getMWHeight(SPACE_BETWEEN, BELOW_NAME);
            renderMWLabel(entity, text, matrices, vertexConsumers, light, bl, fl, i);
            ci.cancel();
        }
    }

    public void renderMWLabel(AbstractClientPlayerEntity entity, Text name, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, boolean bl, float fl, int i) throws FileNotFoundException, ParseException {
        MWPlayer mwPlayer = getMWPlayer(entity.getUuid());
        String ts = String.valueOf((int) round(mwPlayer.getSkill().getConservativeRating()));
        int ranking = mwPlayer.getRanking() + 1;
        Text text = new TranslatableText("render.urmwhelper.label", ts, ranking + getSuffix(ranking)).formatted(Formatting.GRAY);

        matrices.push();
        matrices.translate(0.0, fl, 0.0);
        matrices.multiply(dispatcher.getRotation());
        matrices.scale(-0.025f, -0.025f, 0.025f);

        Matrix4f matrix4f = matrices.peek().getModel();
        float g = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25f);
        int j = (int)(g * 255.0f) << 24;
        TextRenderer textRenderer = this.getTextRenderer();
        float h = -textRenderer.getWidth(text) / 2f;
        textRenderer.draw(text, h, (float)i, 0x20FFFFFF, false, matrix4f, vertexConsumers, bl, j, light);
        if (bl) {
            textRenderer.draw(text, h, (float)i, -1, false, matrix4f, vertexConsumers, false, 0, light);
        }
        matrices.pop();


        //for normal nametag
        int nameColor = getColor(mwPlayer.getRankName());
        Text playerName;
        if (nameColor != -1 && Rendering.doRenderColor){
            playerName = new LiteralText(name.getString()).setStyle(Style.EMPTY.withColor(getColor(mwPlayer.getRankName())));
        }else {
            playerName = name;
        }

        float f = entity.getHeight() + getVanillaHeight(SPACE_BETWEEN, BELOW_NAME);
        matrices.push();
        matrices.translate(0.0, f, 0.0);
        matrices.multiply(dispatcher.getRotation());
        matrices.scale(-0.025f, -0.025f, 0.025f);
        Matrix4f matrix4f2 = matrices.peek().getModel();
        float h2 = -textRenderer.getWidth(name) / 2f;
        textRenderer.draw(playerName, h2, (float)i, 0x20FFFFFF, false, matrix4f2, vertexConsumers, bl, j, light);
        if (bl) {
            textRenderer.draw(playerName, h2, (float)i, -1, false, matrix4f2, vertexConsumers, false, 0, light);
        }
        matrices.pop();
    }

    private int getColor(String rankName) {
        if ("grand champion".equals(rankName)){
            return 0xC71E5C;
        }else if("champion".equals(rankName)){
            return 0xB11E89;
        }else if("diamond".equals(rankName)){
            return 0x00FFD4;
        }else if("platinum".equals(rankName)){
            return 0x85DFCC;
        }else if("gold".equals(rankName)){
            return 0xF1C40F;
        }else if("silver".equals(rankName)){
            return 0xD9E1E9;
        }else if("bronze".equals(rankName)){
            return 0xB84526;
        }
        return -1;
    }

    private float getVanillaHeight(float spaceBetween, boolean belowName){
        if(belowName){
            return 0.5f + spaceBetween;
        }else {
            return 0.5f;
        }
    }

    private float getMWHeight(float spaceBetween, boolean belowName) {
        if (belowName){
            return 0.5f;
        }else {
            return 0.5f + spaceBetween;
        }
    }

    private static String getSuffix(int n) {
        if ((n / 10) % 10 == 1) return "th";
        return switch (n % 10) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }

    private static MWPlayer getMWPlayer(UUID uuid) throws FileNotFoundException, ParseException {
        MWPlayer mwPlayer;
        if (mwPlayerMap.getOrDefault(uuid, null) != null){
            mwPlayer = mwPlayerMap.get(uuid);
        }else {
            mwPlayer = new MWPlayer(uuidMap.get(uuid), true);
            mwPlayerMap.put(uuid, mwPlayer);
        }
        return mwPlayer;
    }


    public boolean doMWRendering(AbstractClientPlayerEntity entity) throws IOException {
        if (!Rendering.doRendering) return false;
        if (entity.isInvisibleTo(URMWHelper.minecraftClient.player)) return false;
        if (entity.isSpectator()) return false;
        tryUpdatePlayers();
        for (UUID uuid : uuidMap.keySet()){
            if (entity.getUuid().equals(uuid)) return true;
        }
        return false;
    }

    private static void tryUpdatePlayers() throws IOException {
        coolDown--;
        assert URMWHelper.minecraftClient.player != null;
        if (URMWHelper.minecraftClient.player.getEntityWorld().getTime() % 6000 == 0 && coolDown < 0){
            Runnable r = new UpdatePlayers();
            coolDown = 200;
            new Thread(r).start();
            mwPlayerMap = new HashMap<>();
        }

    }

    public TextRenderer getTextRenderer() {
        return this.textRenderer;
    }
}
