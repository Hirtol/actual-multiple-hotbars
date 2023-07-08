package top.hirtol.actualmultiplehotbars.mixin;


import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import top.hirtol.actualmultiplehotbars.client.AMHClientState;
import top.hirtol.actualmultiplehotbars.config.AMHConfig;
import top.hirtol.actualmultiplehotbars.inventory.ServerInventoryManager;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {

  private static final Logger logger = LoggerFactory.getLogger(PlayerInventoryMixin.class);

  @Final
  @Shadow
  public PlayerEntity player;

  @Shadow
  public int selectedSlot;

  @Inject(method = "addStack(Lnet/minecraft/item/ItemStack;)I",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;addStack(ILnet/minecraft/item/ItemStack;)I"),
      locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
  private void preferExtraHotbarPickup(ItemStack stack, CallbackInfoReturnable<Integer> cir, int i) {
    var config = AMHConfig.getInstance();
    var hotbar = ServerInventoryManager.getPlayerState(player);

    if (hotbar != null) {
      // Check if this pickup would've gone into the player's main hotbar. If so, allow it if the config is set.
      // Lastly, check that we're allowed to insert into the additional hotbars at all.
      if ((i >= 0 && i < 9 && !config.preferAdditionalOverMain()) || !config.allowFillingAdditional()) {
        return;
      }

      var slot = hotbar.getOccupiedSlotWithRoomForStack(stack);

      if (slot != -1) {
        cir.setReturnValue(hotbar.addStackTillMax(slot, stack));
        hotbar.markDirty();
      } else if (config.allowFillingEmptyAdditional()) {
        slot = hotbar.getEmptySlot();
        if (slot != -1) {
          cir.setReturnValue(hotbar.addStackTillMax(slot, stack));
          hotbar.markDirty();
        }
      }
    }
  }

  @Inject(method = "updateItems", at = @At(value = "HEAD"))
  private void updateHotbarItems(CallbackInfo ci) {
    var hotbar = ServerInventoryManager.getPlayerState(player);

    if (hotbar != null) {
      for (int i = 0; i < hotbar.getItems().size(); ++i) {
        if (hotbar.getStack(i).isEmpty()) {
          continue;
        }
        hotbar.getStack(i).inventoryTick(this.player.world, this.player, i, false);
      }
    }
  }

  @Inject(method = "scrollInHotbar", at = @At(value = "HEAD"))
  private void allowScrollingToNextBar(double scrollAmount, CallbackInfo ci) {
    var hotbar = AMHClientState.getInstance().getHotbarInventory();

    if (AMHConfig.getInstance().getClientSettings().allowScrollSwap && hotbar != null
        && this.player.world.isClient) {
      int newSlot = selectedSlot - (int) Math.signum(scrollAmount);
      if (newSlot < 0) {
        AMHClientState.getInstance().getProvider().rotate((ClientPlayerEntity) player, true);
      } else if (newSlot >= 9) {
        AMHClientState.getInstance().getProvider().rotate((ClientPlayerEntity) player, false);
      }
    }
  }
}