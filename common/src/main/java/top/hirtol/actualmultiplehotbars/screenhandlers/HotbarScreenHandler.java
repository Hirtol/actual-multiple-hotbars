package top.hirtol.actualmultiplehotbars.screenhandlers;

import java.util.Objects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.ActualHotbars;
import top.hirtol.actualmultiplehotbars.client.AMHClientState;
import top.hirtol.actualmultiplehotbars.inventory.HotbarInventory;
import top.hirtol.actualmultiplehotbars.inventory.PlayerHotbarState;

//GenericContainerScreenHandler
public class HotbarScreenHandler extends ScreenHandler {

  private static final Logger logger = LoggerFactory.getLogger(HotbarScreenHandler.class);
  public static final Identifier HOTBAR_SCREEN_ID = ActualHotbars.ID("hotbar_screen_handler");

  public HotbarInventory hotbarInventory;

  public HotbarScreenHandler(int syncId, PlayerInventory playerInventory) {
    this(syncId, playerInventory,
        Objects.requireNonNullElseGet(AMHClientState.getInstance().getHotbarInventory(), HotbarInventory::new));
  }

  public HotbarScreenHandler(int syncId, PlayerInventory playerInventory, HotbarInventory inventory) {
    super(ScreenHandlers.HOTBAR_SCREEN.get(), syncId);

    this.hotbarInventory = inventory;
    inventory.onOpen(playerInventory.player);

    int rows = inventory.getRowCount();
    int i = (rows - 4) * 18;

    for (int j = 0; j < rows; ++j) {
      int physicalIndex = inventory.getVirtualState().virtualPhysicalMappings.getInt(j + 1);
      int inventoryRowIndex = PlayerHotbarState.toInventoryRowIndex(physicalIndex);
      Inventory invToUse = physicalIndex == PlayerHotbarState.MAIN_HOTBAR_INDEX ? playerInventory : inventory;

      for (int k = 0; k < 9; ++k) {
        int index = k + (inventoryRowIndex * inventory.getColumnCount());
        this.addSlot(new Slot(invToUse, index, 8 + k * 18, 18 + j * 18));
      }
    }

    for (int j = 0; j < 3; ++j) {
      for (int k = 0; k < 9; ++k) {
        this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
      }
    }

    for (int j = 0; j < 9; ++j) {
      int physicalIndex = inventory.getVirtualState().virtualPhysicalMappings.getInt(0);
      int inventoryRowIndex = PlayerHotbarState.toInventoryRowIndex(physicalIndex);
      Inventory invToUse = physicalIndex == PlayerHotbarState.MAIN_HOTBAR_INDEX ? playerInventory : inventory;
      int index = j + (inventoryRowIndex * inventory.getColumnCount());
      this.addSlot(new Slot(invToUse, index, 8 + j * 18, 161 + i));
    }
  }

  @Override
  public boolean canUse(PlayerEntity player) {
    return this.hotbarInventory.canPlayerUse(player);
  }

  @Override
  public ItemStack transferSlot(PlayerEntity player, int index) {
    ItemStack itemStack = ItemStack.EMPTY;
    Slot slot = (Slot) this.slots.get(index);
    if (slot != null && slot.hasStack()) {
      ItemStack itemStack2 = slot.getStack();
      itemStack = itemStack2.copy();
      if (index < this.hotbarInventory.getItems().size() ? !this.insertItem(itemStack2,
          this.hotbarInventory.getItems().size(), this.slots.size(), true)
          : !this.insertItem(itemStack2, 0, this.hotbarInventory.getItems().size(), false)) {
        return ItemStack.EMPTY;
      }
      if (itemStack2.isEmpty()) {
        slot.setStack(ItemStack.EMPTY);
      } else {
        slot.markDirty();
      }
    }
    return itemStack;
  }

  @Override
  public void close(PlayerEntity player) {
    super.close(player);
    this.hotbarInventory.onClose(player);
  }
}
