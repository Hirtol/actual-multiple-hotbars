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
import top.hirtol.actualmultiplehotbars.client.MultiClientState;
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
    var settings = MultiClientState.getInstance().config().getClientSettings();

    for (int i = 0; i < settings.numberOfAdditionalVisibleHotbars; i++) {
      drawTexture(matrices, this.scaledWidth / 2 - 91,
          this.scaledHeight - 22 - (settings.shift * (i + 1)), 0, 0, 182,
          22);
    }

    this.onScreen = true;
  }

//  @Inject(method = "renderHotbar", at = @At(value = "HEAD"))
//  private void startRenderScaledUi(float tickDelta, MatrixStack matrices, CallbackInfo ci) {
//    matrices.push();
//    matrices.scale(0.83f, 0.83f, 1.0f);
//    this.scaledWidth *= 1.2;
//    this.scaledHeight *= 1.2;
//  }
//
//  @Inject(method = "renderHotbar", at = @At(value = "RETURN"))
//  private void endRenderScaledUi(float tickDelta, MatrixStack matrices, CallbackInfo ci) {
//    matrices.pop();
//    this.scaledWidth /= 1.2;
//    this.scaledHeight /= 1.2;
//  }

  @Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 1))
  private void shiftHotbarSelector(float tickDelta, MatrixStack matrices, CallbackInfo info) {
    var settings = MultiClientState.getInstance().config().getClientSettings();
    if (settings.reverseBars) {
      matrices.translate(0, -(settings.shift * settings.numberOfAdditionalVisibleHotbars), 0);
    }
  }

  @Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 0))
  private void returnHotbarSelector(float tickDelta, MatrixStack matrices, CallbackInfo info) {
    var settings = MultiClientState.getInstance().config().getClientSettings();
    if (settings.reverseBars) {
      matrices.translate(0, (settings.shift * settings.numberOfAdditionalVisibleHotbars), 0);
    }
  }

  @Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;setZOffset(I)V", ordinal = 1))
  private void shiftHotbarItems(float tickDelta, MatrixStack matrices, CallbackInfo info) {
    var settings = MultiClientState.getInstance().config().getClientSettings();
    if (settings.reverseBars) {
      this.scaledHeight -= (settings.shift * settings.numberOfAdditionalVisibleHotbars);
    }
  }

  @Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 1))
  private void renderHotbarItems(float tickDelta, MatrixStack matrices, CallbackInfo info) {
    var settings = MultiClientState.getInstance().config().getClientSettings();

    if (settings.reverseBars) {
      this.scaledHeight += (settings.shift * (settings.numberOfAdditionalVisibleHotbars));
    }

    for (int hotbarI = 0; hotbarI < settings.numberOfAdditionalVisibleHotbars; hotbarI++) {
      int shift = (settings.shift * (settings.reverseBars ? settings.numberOfAdditionalVisibleHotbars - (hotbarI + 1): hotbarI + 1));
      int p = this.scaledHeight - 16 - 3 - shift;
      int m = 1;

      int physicalHotbarIndex = MultiClientState.getInstance().getHotbarInventory().getVirtualState().virtualPhysicalMappings.getInt(hotbarI + 1);
      int inventoryIndex = PlayerHotbarState.toInventoryRowIndex(physicalHotbarIndex);

      if (physicalHotbarIndex == PlayerHotbarState.MAIN_HOTBAR_INDEX) {
        for (int slotI = 0; slotI < 9; ++slotI) {
          int o = this.scaledWidth / 2 - 90 + slotI * 20 + 2;

          ItemStack item = getCameraPlayer().getInventory().getStack(inventoryIndex + slotI);
          this.renderHotbarItem(o, p, tickDelta, getCameraPlayer(), item, m++);
        }
      } else {
        int rowStart = inventoryIndex * 9;

        for (int slotI = 0; slotI < 9; ++slotI) {
          int o = this.scaledWidth / 2 - 90 + slotI * 20 + 2;

          ItemStack item = MultiClientState.getInstance().getProvider().getItem(rowStart + slotI);
          this.renderHotbarItem(o, p, tickDelta, getCameraPlayer(), item, m++);
        }
      }
    }
  }

  @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;hasStatusBars()Z"))
  public void shiftStatusBars(MatrixStack matrices, float tickDelta, CallbackInfo info) {
    if (this.onScreen) {
      var settings = MultiClientState.getInstance().config().getClientSettings();
      matrices.translate(0, -(settings.shift * settings.numberOfAdditionalVisibleHotbars), 0);
    }
  }

  @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getSleepTimer()I", ordinal = 0))
  public void returnStatusBars(MatrixStack matrices, float tickDelta, CallbackInfo info) {
    if (this.onScreen) {
      var settings = MultiClientState.getInstance().config().getClientSettings();
      matrices.translate(0, (settings.shift * settings.numberOfAdditionalVisibleHotbars), 0);
    }
    this.onScreen = false;
  }

}