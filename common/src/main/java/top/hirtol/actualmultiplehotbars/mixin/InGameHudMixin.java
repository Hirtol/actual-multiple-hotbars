package top.hirtol.actualmultiplehotbars.mixin;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hirtol.actualmultiplehotbars.client.AMHClientState;
import top.hirtol.actualmultiplehotbars.config.AMHConfig;
import top.hirtol.actualmultiplehotbars.config.AMHConfigData.ClientSettings;
import top.hirtol.actualmultiplehotbars.inventory.PlayerHotbarState;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {

  private static final Logger logger = LoggerFactory.getLogger(InGameHudMixin.class);
  private boolean onScreen = false;
  @Shadow
  private int scaledHeight;
  @Shadow
  private int scaledWidth;

  @Shadow
  protected abstract PlayerEntity getCameraPlayer();

  @Shadow
  protected abstract void renderHotbarItem(int x, int y, float tickDelta, PlayerEntity player, ItemStack stack,
      int seed);

  @Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 0))
  private void renderHotbarFrame(float tickDelta, MatrixStack matrices, CallbackInfo info) {
    var settings = AMHConfig.getInstance().getClientSettings();

    for (int i = 0; i < settings.additionalVisibleHotbars; i++) {
      drawTexture(matrices, this.scaledWidth / 2 - 91,
          this.scaledHeight - 22 - (settings.shift * (i + 1)), 0, 0, 182,
          22);
    }

    this.onScreen = true;
  }

  @Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 1))
  private void shiftHotbarSelector(float tickDelta, MatrixStack matrices, CallbackInfo info) {
    var settings = AMHConfig.getInstance().getClientSettings();
    if (settings.reverseBars) {
      matrices.translate(0, -(settings.shift * settings.additionalVisibleHotbars), 0);
    }
  }

  @Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 0))
  private void returnHotbarSelector(float tickDelta, MatrixStack matrices, CallbackInfo info) {
    var settings = AMHConfig.getInstance().getClientSettings();
    if (settings.reverseBars) {
      matrices.translate(0, (settings.shift * settings.additionalVisibleHotbars), 0);
    }
  }

  @Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;setZOffset(I)V", ordinal = 1))
  private void shiftHotbarItems(float tickDelta, MatrixStack matrices, CallbackInfo info) {
    var settings = AMHConfig.getInstance().getClientSettings();
    if (settings.reverseBars) {
      this.scaledHeight -= (settings.shift * settings.additionalVisibleHotbars);
    }
  }

  @Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 1))
  private void renderHotbarItems(float tickDelta, MatrixStack matrices, CallbackInfo info) {
    ClientSettings settings = AMHConfig.getInstance().getClientSettings();
    AMHClientState state = AMHClientState.getInstance();

    if (state.getHotbarInventory() == null) {
      return;
    }
    if (settings.reverseBars) {
      this.scaledHeight += (settings.shift * (settings.additionalVisibleHotbars));
    }

    int m = 1;

    for (int hotbarI = 1; hotbarI <= settings.additionalVisibleHotbars; hotbarI++) {
      int shift = settings.shift * (settings.reverseBars ? settings.additionalVisibleHotbars - hotbarI : hotbarI);
      int p = this.scaledHeight - 16 - 3 - shift;
      int physicalHotbarIndex = state.getHotbarInventory().getVirtualState().getPhysicalFromVisual(hotbarI);
      int inventoryIndex = PlayerHotbarState.toInventoryRowIndex(physicalHotbarIndex);

      if (physicalHotbarIndex == PlayerHotbarState.MAIN_HOTBAR_INDEX) {
        for (int slotI = 0; slotI < PlayerHotbarState.VANILLA_HOTBAR_SIZE; ++slotI) {
          int o = (this.scaledWidth / 2) - 90 + (slotI * 20) + 2;

          ItemStack item = getCameraPlayer().getInventory().getStack(inventoryIndex + slotI);
          this.renderHotbarItem(o, p, tickDelta, getCameraPlayer(), item, m++);
        }
      } else {
        int rowStart = inventoryIndex * PlayerHotbarState.VANILLA_HOTBAR_SIZE;

        for (int slotI = 0; slotI < PlayerHotbarState.VANILLA_HOTBAR_SIZE; ++slotI) {
          int o = this.scaledWidth / 2 - 90 + slotI * 20 + 2;

          ItemStack item = state.getProvider().getItem(rowStart + slotI);
          this.renderHotbarItem(o, p, tickDelta, getCameraPlayer(), item, m++);
        }
      }
    }
  }

  @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;hasStatusBars()Z"))
  public void shiftStatusBars(MatrixStack matrices, float tickDelta, CallbackInfo info) {
    if (this.onScreen) {
      var settings = AMHConfig.getInstance().getClientSettings();
      matrices.translate(0, -(settings.shift * settings.additionalVisibleHotbars), 0);
    }
  }

  @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getSleepTimer()I", ordinal = 0))
  public void returnStatusBars(MatrixStack matrices, float tickDelta, CallbackInfo info) {
    if (this.onScreen) {
      var settings = AMHConfig.getInstance().getClientSettings();
      matrices.translate(0, (settings.shift * settings.additionalVisibleHotbars), 0);
    }
    this.onScreen = false;
  }

}