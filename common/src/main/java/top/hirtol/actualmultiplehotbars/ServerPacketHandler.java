package top.hirtol.actualmultiplehotbars;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.inventory.ServerInventoryManager;

public class ServerPacketHandler {

  private static final Logger logger = LoggerFactory.getLogger(ServerPacketHandler.class);

  public static void swapRow(ServerPlayerEntity player, int fromHotbarIndex, int toHotbarIndex) {
    var state = ServerInventoryManager.getPlayerState(player);
    var playerInv = player.getInventory();

    if (toHotbarIndex < 0 || toHotbarIndex > state.getRowCount() || fromHotbarIndex < 0
        || fromHotbarIndex > state.getRowCount()) {
      logger.warn("Player {} attempted to use invalid row swap index: `{}` - `{}`", player.getDisplayName().getString(),
          fromHotbarIndex, toHotbarIndex);
      return;
    }

    Inventory fromInventory = fromHotbarIndex == 0 ? playerInv : state;
    Inventory toInventory = toHotbarIndex == 0 ? playerInv : state;

    // Get the correct index, taking into account that the external inventory once again starts at rowIndex 0, but
    // we've overloaded rowIndex `0` to mean the player's actual hotbar at the moment, therefore subtract 1.
    int baseFrom = Math.max(fromHotbarIndex - 1, 0) * state.getColumnCount();
    int baseTo = Math.max(toHotbarIndex - 1, 0) * state.getColumnCount();

    List<ItemStack> oldFrom = new ArrayList<>();
    List<ItemStack> oldTo = new ArrayList<>();

    for (int i = baseFrom; i < baseFrom + state.getColumnCount(); i++) {
      oldFrom.add(fromInventory.removeStack(i));
    }
    for (int i = baseTo; i < baseTo + state.getColumnCount(); i++) {
      oldTo.add(toInventory.removeStack(i));
    }

    for (int i = baseFrom; i < baseFrom + state.getColumnCount(); i++) {
      fromInventory.setStack(i, oldTo.get(i - baseFrom));
    }
    for (int i = baseTo; i < baseTo + state.getColumnCount(); i++) {
      toInventory.setStack(i, oldFrom.get(i - baseTo));
    }

    fromInventory.markDirty();
    toInventory.markDirty();
  }
}
