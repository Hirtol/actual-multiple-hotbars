package top.hirtol.actualmultiplehotbars.forge.mixin;


import net.minecraft.client.util.math.MatrixStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.ForgeIngameGui;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hirtol.actualmultiplehotbars.config.AMHConfig;

@OnlyIn(Dist.CLIENT)
@Mixin(ForgeIngameGui.class)
public abstract class ForgeGuiMixin {

  private static final Logger logger = LoggerFactory.getLogger(ForgeGuiMixin.class);

  @Inject(method = "renderHealth", at = @At(value = "HEAD"))
  public void shiftStatusBars(int width, int height, MatrixStack pStack, CallbackInfo info) {
      var settings = AMHConfig.getInstance().getClientSettings();
    pStack.translate(0, -(settings.shift * settings.additionalVisibleHotbars), 0);
  }

  @Inject(method = "renderSleepFade", at = @At(value = "HEAD"))
  public void returnStatusBars(int width, int height, MatrixStack poseStack, CallbackInfo info) {
      var settings = AMHConfig.getInstance().getClientSettings();
    poseStack.translate(0, (settings.shift * settings.additionalVisibleHotbars), 0);
  }
}
