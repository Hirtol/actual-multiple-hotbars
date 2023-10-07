package top.hirtol.actualmultiplehotbars.forge.integrations.mixins;


import iskallia.vault.container.inventory.ShardPouchContainer;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.ItemShardPouch;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.hirtol.actualmultiplehotbars.inventory.HotbarInventory;
import top.hirtol.actualmultiplehotbars.inventory.ServerInventoryManager;

@Mixin(PlayerInventory.class)
public abstract class TheVaultInterceptItemMixin {

  private static final Logger logger = LoggerFactory.getLogger(TheVaultInterceptItemMixin.class);

  @Final
  @Shadow
  public PlayerEntity player;

  /**
   * Copied straight from `the_vault` source code. As their intercept is done in a Mixin we can't modify it directly, so
   * we'll just have to pre-empt them.
   *
   * @param stack
   * @param cir
   */
  @Inject(method = "addStack(Lnet/minecraft/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
  public void interceptItemAddition(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
    if (stack.getItem() == ModItems.SOUL_SHARD) {
      if (!(this.player.currentScreenHandler instanceof ShardPouchContainer)) {
        HotbarInventory inv = ServerInventoryManager.getPlayerState(this.player);
        ItemStack pouchStack = ItemStack.EMPTY;

        for (int slot = 0; slot < inv.size(); ++slot) {
          ItemStack invStack = inv.getStack(slot);
          if (invStack.getItem() instanceof ItemShardPouch) {
            pouchStack = invStack;
            break;
          }
        }

        if (!pouchStack.isEmpty()) {
          pouchStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent((handler) -> {
            ItemStack remainder = handler.insertItem(0, stack, false);
            stack.setCount(remainder.getCount());
            if (stack.isEmpty()) {
              cir.setReturnValue(0);
            }

          });
        }
      }
    }
  }
}
