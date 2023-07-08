package top.hirtol.actualmultiplehotbars.client.providers;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;

public interface HotbarInventoryProvider {

  /**
   * Get the current item at index `i` of the Provider's inventory.
   * @param i
   */
  ItemStack getItem(int i);

  /**
   * Reset current state to the default.
   * Useful if the config is being changed.
   */
  void reset();

  /**
   * Swaps the current hotbar with the given {@code row} of the provider's inventory.
   * @param player The player to swap
   * @param row The row to swap
   */
  void equipVirtualHotbar(ClientPlayerEntity player, int row);

  /**
   * Rotate the current hotbars
   * @param player The player for whom to rotate.
   */
  void rotate(ClientPlayerEntity player, boolean reverse);
}
