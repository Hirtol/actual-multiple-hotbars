package top.hirtol.actualmultiplehotbars.mixin;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hirtol.actualmultiplehotbars.client.MultiClientState;
import top.hirtol.actualmultiplehotbars.config.AMHConfigData.ClientSettings;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {

  private static final Logger logger = LogManager.getLogger(InGameHudMixin.class);
  private boolean onScreen = false;
  @Shadow
  private int scaledHeight;
  @Shadow
  private int scaledWidth;

  @Shadow
  protected abstract PlayerEntity getCameraPlayer();

  @Shadow
  protected abstract void renderHotbarItem(int x, int y, float tickDelta, PlayerEntity player, ItemStack stack);

  @Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 0))
  private void renderHotbarFrame(float tickDelta, MatrixStack matrices, CallbackInfo info) {
    ClientSettings settings = MultiClientState.getInstance().config().getClientSettings();

    for (int i = 0; i < settings.numberOfAdditionalVisibleHotbars; i++) {
      drawTexture(matrices, this.scaledWidth / 2 - 91,
          this.scaledHeight - 22 - (settings.shift * (i + 1)), 0, 0, 182,
          22);
    }

    this.onScreen = true;
  }

  @Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 1))
  private void shiftHotbarSelector(float tickDelta, MatrixStack matrices, CallbackInfo info) {
    ClientSettings settings = MultiClientState.getInstance().config().getClientSettings();
    if (settings.reverseBars) {
      matrices.translate(0, -(settings.shift * settings.numberOfAdditionalVisibleHotbars), 0);
    }
  }

  @Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 0))
  private void returnHotbarSelector(float tickDelta, MatrixStack matrices, CallbackInfo info) {
    ClientSettings settings = MultiClientState.getInstance().config().getClientSettings();
    if (settings.reverseBars) {
      matrices.translate(0, (settings.shift * settings.numberOfAdditionalVisibleHotbars), 0);
    }
  }

  @Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;setZOffset(I)V", ordinal = 1))
  private void shiftHotbarItems(float tickDelta, MatrixStack matrices, CallbackInfo info) {
    ClientSettings settings = MultiClientState.getInstance().config().getClientSettings();
    if (settings.reverseBars) {
      this.scaledHeight -= (settings.shift * settings.numberOfAdditionalVisibleHotbars);
    }
  }

  @Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 1))
  private void renderHotbarItems(float tickDelta, MatrixStack matrices, CallbackInfo info) {
    ClientSettings settings = MultiClientState.getInstance().config().getClientSettings();

    if (settings.reverseBars) {
      this.scaledHeight += (settings.shift * (settings.numberOfAdditionalVisibleHotbars));
    }

    for (int hotbarI = 0; hotbarI < settings.numberOfAdditionalVisibleHotbars; hotbarI++) {
      for (int slotI = 0; slotI < 9; ++slotI) {
        int o = this.scaledWidth / 2 - 90 + slotI * 20 + 2;
        int shift = (settings.shift * (settings.reverseBars ? settings.numberOfAdditionalVisibleHotbars - (hotbarI + 1): hotbarI + 1));
        int p =
            this.scaledHeight - 16 - 3 - shift;
        int rowStart = hotbarI * 9;
        ItemStack item = MultiClientState.getInstance().getProvider().getItem(rowStart + slotI);
        this.renderHotbarItem(o, p, tickDelta, getCameraPlayer(), item);
      }
    }
  }

  @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;hasStatusBars()Z"))
  public void shiftStatusBars(MatrixStack matrices, float tickDelta, CallbackInfo info) {
    if (this.onScreen) {
      ClientSettings settings = MultiClientState.getInstance().config().getClientSettings();
      matrices.translate(0, -(settings.shift * settings.numberOfAdditionalVisibleHotbars), 0);
    }
  }

  @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getSleepTimer()I", ordinal = 0))
  public void returnStatusBars(MatrixStack matrices, float tickDelta, CallbackInfo info) {
    if (this.onScreen) {
      ClientSettings settings = MultiClientState.getInstance().config().getClientSettings();
      matrices.translate(0, (settings.shift * settings.numberOfAdditionalVisibleHotbars), 0);
    }
    this.onScreen = false;
  }

}