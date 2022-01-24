package net.winniethedampoeh.urmwhelper.mixin;


import net.minecraft.client.gui.screen.TitleScreen;
import net.winniethedampoeh.urmwhelper.URMWHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class URMWHelperMixin {
    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo info) {
        URMWHelper.LOGGER.info("This line is printed by URMW-Helper!");
    }
}
