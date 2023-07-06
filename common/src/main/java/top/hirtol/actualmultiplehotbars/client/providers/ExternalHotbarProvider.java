package top.hirtol.actualmultiplehotbars.client.providers;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.client.MultiClientState;
import top.hirtol.actualmultiplehotbars.config.Config;
import top.hirtol.actualmultiplehotbars.inventory.PlayerHotbarState;
import top.hirtol.actualmultiplehotbars.networking.packets.HotbarRotateC2SPacket;
import top.hirtol.actualmultiplehotbars.networking.packets.HotbarSetVirtualC2SPacket;

/**
 * Will use a seperately stored inventory (with say, n*9 capacity, where `n` is the amount of hotbars we allow). When we
 * want to get the available items we then query this external inventory.
 * <p>
 * We can hook several PlayerInventory (e.g., 'getMainHandStack', 'isValidHotbarSlot', etc, see multi-hotbar-core for
 * inspiration) to then allow direct access.
 * <p>
 * Alternatively we could also allow the simpler method of having the player pull items out of that inventory, swap it
 * into their current active hotbar, and then swap the old hotbar items into the inventory. Benefit here is that this is
 * guaranteed to be compatible with every other mod, but has some caveats about user friendlyness.
 */
public class ExternalHotbarProvider implements HotbarInventoryProvider {

  private static final Logger logger = LoggerFactory.getLogger(ExternalHotbarProvider.class);

  private int currentSwapIndex = 1;
  private Config config = MultiClientState.getInstance().config();

  @Override
  public ItemStack getItem(int i) {
    var inventory = MultiClientState.getInstance().getHotbarInventory();

    if (inventory != null) {
      return inventory.getStack(i);
    } else {
      return ItemStack.EMPTY;
    }
  }

  @Override
  public void reset() {

  }

  @Override
  public void swapRow(ClientPlayerEntity player, int row) {
    var packet = new HotbarSetVirtualC2SPacket(PlayerHotbarState.MAIN_HOTBAR_INDEX, row);

    packet.send();
  }

  @Override
  public void rotate(ClientPlayerEntity player) {
    var packet = new HotbarRotateC2SPacket(this.getMaxRowIndexExcl());
    packet.send();
  }

  private int nextSwapIndex(int currentSwapIndex) {
    if (getMaxRowIndexExcl() == currentSwapIndex) {
      return 0;
    } else {
      return currentSwapIndex + 1;
    }
  }

  private int previousSwapIndex(int currentSwapIndex) {
    if (currentSwapIndex == 1) {
      return getMaxRowIndexExcl();
    } else {
      return currentSwapIndex - 1;
    }
  }

  private int getMaxRowIndexExcl() {
    int additionalPossible = Math.max(this.config.getAdditionalHotbars() - 1, 0);
    int maxVisibleIndex = Math.max(this.config.getClientSettings().numberOfAdditionalVisibleHotbars, 0);

    return (this.config.getClientSettings().rotateBeyondVisible ?
        additionalPossible
        : Math.min(additionalPossible, maxVisibleIndex)) + 1;
  }
}
