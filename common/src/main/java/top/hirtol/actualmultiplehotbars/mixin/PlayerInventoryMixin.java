package top.hirtol.actualmultiplehotbars.mixin;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import top.hirtol.actualmultiplehotbars.config.Config;
import top.hirtol.actualmultiplehotbars.ServerState;
import top.hirtol.actualmultiplehotbars.inventory.HotbarInventory;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {

  private static final Logger logger = LogManager.getLogger(PlayerInventoryMixin.class);

  @Final
  @Shadow
  public PlayerEntity player;

  @Inject(method = "addStack(Lnet/minecraft/item/ItemStack;)I",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;addStack(ILnet/minecraft/item/ItemStack;)I"),
      locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
  private void preferExtraHotbarPickup(ItemStack stack, CallbackInfoReturnable<Integer> cir, int i) {
    Config config = Config.getInstance();
    HotbarInventory hotbar = ServerState.getPlayerState(player);

    if (hotbar != null) {
      // Check if this pickup would've gone into the player's main hotbar. If so, allow it if the config is set.
      // Lastly, check that we're allowed to insert into the additional hotbars at all.
      if ((i >= 0 && i < 9 && !config.preferAdditionalOverMain()) || !config.allowFillingAdditional()) {
        return;
      }

      int slot = hotbar.getOccupiedSlotWithRoomForStack(stack);

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
    HotbarInventory hotbar = ServerState.getPlayerState(player);

    if (hotbar != null) {
      for (int i = 0; i < hotbar.getItems().size(); ++i) {
        if (hotbar.getStack(i).isEmpty()) {
          continue;
        }
        hotbar.getStack(i).inventoryTick(this.player.world, this.player, i, false);
      }
    }
  }
}