package net.winniethedampoeh.urmwhelper.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.Matrix4f;
import net.winniethedampoeh.urmwhelper.URMWHelper;
import net.winniethedampoeh.urmwhelper.config.Rendering;
import net.winniethedampoeh.urmwhelper.mwapi.MWPlayer;
import net.winniethedampoeh.urmwhelper.parallelthread.UpdatePlayers;
import net.winniethedampoeh.urmwhelper.storage.UuidMap;
import org.json.simple.parser.ParseException;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.lang.Math.round;

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderer.class)
public class NameTagRendererMixin<T extends Entity> {

    @Shadow
    @Final
    private TextRenderer textRenderer;

    private static int coolDown = 0;

    @Shadow
    @Final
    protected EntityRenderDispatcher dispatcher;

    private static Map<UUID, MWPlayer> mwPlayerMap = new HashMap<>();


    @Inject(method = {"render"}, at = {@At(value = "HEAD")})
    private void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) throws IOException, ParseException {
        if (!Rendering.doRendering) return;
        if (entity instanceof PlayerEntity){
            if (entity.isSpectator()) return;
            updatePlayers();
            Map<UUID, String> UUIDMap = URMWHelper.getInstance().UUIDMap;
            for (UUID uuid : UUIDMap.keySet()){
                if (entity.getUuid().equals(uuid)){
                    double dis = dispatcher.getSquaredDistanceToCamera(entity);
                    if (dis <=4096D){
                        boolean bl = !((Entity)entity).isSneaky();
                        MWPlayer mwPlayer;
                        if (mwPlayerMap.getOrDefault(uuid, null) != null){
                            mwPlayer = mwPlayerMap.get(uuid);
                        }else {
                            mwPlayer = new MWPlayer(UUIDMap.get(uuid), true);
                            mwPlayerMap.put(uuid, mwPlayer);
                        }
                        String TS = String.valueOf((int) round(mwPlayer.getSkill().getConservativeRating()));
                        int Ranking = mwPlayer.getRanking() + 1;
                        MutableText text = new LiteralText(TS + " TS, " + Ranking + getSuffix(Ranking));
                        float scale = 0.05f * URMWHelper.getInstance().getConfig().getScale();
                        float f = ((Entity)entity).getHeight() + URMWHelper.getInstance().getConfig().getHeight();
                        int i = "deadmau5".equals(text.getString()) ? -10 : 0;

                        matrices.push();
                        matrices.translate(0.0, f, 0.0);
                        matrices.multiply(dispatcher.getRotation());
                        matrices.scale(-scale, -scale, scale);

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
                    }
                }
            }

        }
    }

    public TextRenderer getTextRenderer() {
        return this.textRenderer;
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

    private static void updatePlayers() throws IOException {
        coolDown--;
        assert URMWHelper.minecraftClient.player != null;
        if (URMWHelper.minecraftClient.player.getEntityWorld().getTime() % 1200 == 0 && coolDown < 0){
            Runnable r = new UpdatePlayers();
            coolDown = 200;
            new Thread(r).start();
            mwPlayerMap = new HashMap<>();
        }

    }


}
