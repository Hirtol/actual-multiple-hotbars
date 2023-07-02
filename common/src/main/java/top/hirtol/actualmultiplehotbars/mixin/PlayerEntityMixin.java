package top.hirtol.actualmultiplehotbars.mixin;


import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hirtol.actualmultiplehotbars.ServerState;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

  private static final Logger logger = LoggerFactory.getLogger(PlayerEntityMixin.class);

  @Final
  @Shadow
  private PlayerInventory inventory;

  @Shadow
  public abstract ItemEntity dropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership);

  @Inject(method = "dropInventory", at = @At(value = "RETURN"))
  private void dropHotbarsOnDeath(CallbackInfo ci) {
    // First ensure we're on the server & we're dealing with a modded player.
    if (!inventory.player.world.isClient && ServerState.isPlayerModded((ServerPlayerEntity) inventory.player)) {
      if (inventory.player.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
        return;
      }

      var hotbarInv = ServerState.getPlayerState((ServerPlayerEntity) inventory.player);
      logger.trace("Dropping additional hotbars for player {} at {}", inventory.player.getDisplayName().getString(), inventory.player.getBlockPos());

      for (int i = 0; i < hotbarInv.getItems().size(); ++i) {
        ItemStack itemStack = hotbarInv.getStack(i);
        if (itemStack.isEmpty()) {
          continue;
        }
        this.dropItem(itemStack, true, false);
        hotbarInv.setStack(i, ItemStack.EMPTY);
      }

      hotbarInv.markDirty();
    }
  }
}