package top.hirtol.actualmultiplehotbars.client.providers;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.hirtol.actualmultiplehotbars.client.AMHClientState;
import top.hirtol.actualmultiplehotbars.config.AMHConfig;
import top.hirtol.actualmultiplehotbars.inventory.HotbarInventory;
import top.hirtol.actualmultiplehotbars.inventory.PlayerHotbarState;
import top.hirtol.actualmultiplehotbars.networking.packets.HotbarRotateC2SPacket;
import top.hirtol.actualmultiplehotbars.networking.packets.HotbarSetVirtualC2SPacket;
import top.hirtol.actualmultiplehotbars.networking.packets.ResetVisualC2SPacket;

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

  private static final Logger logger = LogManager.getLogger(ExternalHotbarProvider.class);

  private final AMHConfig config = AMHConfig.getInstance();

  @Override
  public ItemStack getItem(int i) {
    HotbarInventory inventory = AMHClientState.getInstance().getHotbarInventory();

    if (inventory != null) {
      return inventory.getStack(i);
    } else {
      return ItemStack.EMPTY;
    }
  }

  @Override
  public void reset() {
    ResetVisualC2SPacket reset = new ResetVisualC2SPacket();
    reset.send();
  }

  @Override
  public void equipVirtualHotbar(ClientPlayerEntity player, int virtualHotbarIndex) {
    if (virtualHotbarIndex > config.getAdditionalHotbars()) {
      return;
    }
    this.preventBobAnimation(player, virtualHotbarIndex);

    HotbarSetVirtualC2SPacket
        packet = new HotbarSetVirtualC2SPacket(PlayerHotbarState.MAIN_HOTBAR_INDEX, virtualHotbarIndex);
    packet.send();
  }

  @Override
  public void rotate(ClientPlayerEntity player, boolean reverse) {
    HotbarRotateC2SPacket packet = new HotbarRotateC2SPacket(this.getMaxRowIndexIncl(), reverse);
    // Prevent item bob animation on equip.
    PlayerHotbarState state = AMHClientState.getInstance().getHotbarInventory().getVirtualState();
    int visualRowToEquip = reverse ? config.getClientSettings().additionalVisibleHotbars
        : 1 % config.getClientSettings().totalHotbars();
    int rowToEquip = state.visualVirtualMappings.getInt(visualRowToEquip);

    this.preventBobAnimation(player, rowToEquip);

    packet.send();
  }

  private int getMaxRowIndexIncl() {
    int additionalPossible = Math.max(this.config.getAdditionalHotbars(), 0);
    int maxVisibleIndex = Math.max(this.config.getClientSettings().additionalVisibleHotbars, 0);

    return (this.config.getClientSettings().rotateBeyondVisible ?
        additionalPossible
        : Math.min(additionalPossible, maxVisibleIndex));
  }

  private void preventBobAnimation(ClientPlayerEntity player, int virtualRowToEquip) {
    AMHClientState state = AMHClientState.getInstance();
    HotbarInventory inventory = state.getHotbarInventory();
    int physicalRow = inventory.getVirtualState().virtualPhysicalMappings.getInt(virtualRowToEquip);

    // Update the playerScreenHandler to prevent bob-animations.
    // The ClientPlayNetworkHandler sets these bob animations when a change to the hotbar is made.
    // This seemed difficult to intercept for *just* the hotbars, so opted for this hack instead.
    if (physicalRow != PlayerHotbarState.MAIN_HOTBAR_INDEX) {
      int rowIndex = PlayerHotbarState.toInventoryRowIndex(physicalRow);

      for (int i = 0; i < PlayerHotbarState.VANILLA_HOTBAR_SIZE; i++) {
        int index = (rowIndex * PlayerHotbarState.VANILLA_HOTBAR_SIZE) + i;
        player.playerScreenHandler.setStackInSlot(36 + i, inventory.getStack(index));
      }
    }
  }
}
