package top.hirtol.actualmultiplehotbars.forge.mixin;


import net.minecraft.client.util.math.MatrixStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.ForgeIngameGui;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hirtol.actualmultiplehotbars.client.MultiClientState;
import top.hirtol.actualmultiplehotbars.config.AMHConfigData.ClientSettings;

@OnlyIn(Dist.CLIENT)
@Mixin(ForgeIngameGui.class)
public abstract class ForgeGuiMixin {

  private static final Logger logger = LogManager.getLogger(ForgeGuiMixin.class);

  @Inject(method = "renderHealth", at = @At(value = "HEAD"))
  public void shiftStatusBars(int width, int height, MatrixStack pStack, CallbackInfo info) {
    ClientSettings settings = MultiClientState.getInstance().config().getClientSettings();
    pStack.translate(0, -(settings.shift * settings.numberOfAdditionalVisibleHotbars), 0);
  }

  @Inject(method = "renderSleepFade", at = @At(value = "HEAD"))
  public void returnStatusBars(int width, int height, MatrixStack poseStack, CallbackInfo info) {
    ClientSettings settings = MultiClientState.getInstance().config().getClientSettings();
    poseStack.translate(0, (settings.shift * settings.numberOfAdditionalVisibleHotbars), 0);
  }
}
