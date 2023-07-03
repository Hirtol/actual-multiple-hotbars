package top.hirtol.actualmultiplehotbars.forge.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hirtol.actualmultiplehotbars.client.MultiClientState;
import top.hirtol.actualmultiplehotbars.config.AMHConfigData.ClientSettings;

@OnlyIn(Dist.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

  private static final Logger logger = LogManager.getLogger(top.hirtol.actualmultiplehotbars.mixin.InGameHudMixin.class);

  @Inject(method = "renderHeldItemTooltip", at = @At(value = "HEAD"))
  public void shiftIfNecessaryTooltip(MatrixStack matrices, CallbackInfo info) {
    ClientSettings settings = MultiClientState.getInstance().config().getClientSettings();
    // In case the survival elements are skipped.
    boolean shouldDrawSurvivalElements = MinecraftClient.getInstance().interactionManager.hasStatusBars() && MinecraftClient.getInstance().getCameraEntity() instanceof PlayerEntity;
    if (!MinecraftClient.getInstance().options.hudHidden && !shouldDrawSurvivalElements) {
      matrices.translate(0, -(settings.shift * settings.numberOfAdditionalVisibleHotbars), 0);
    }
  }
}
