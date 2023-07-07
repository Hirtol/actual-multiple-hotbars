package top.hirtol.actualmultiplehotbars.screenhandlers.forge;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.ActualHotbars;
import top.hirtol.actualmultiplehotbars.forge.ActualHotbarsForge;
import top.hirtol.actualmultiplehotbars.inventory.HotbarInvState;

//GenericContainerScreenHandler
public class HotbarScreenHandler extends ScreenHandler {

  private static final Logger logger = LoggerFactory.getLogger(HotbarScreenHandler.class);
  public static final Identifier HOTBAR_SCREEN_ID = new Identifier(ActualHotbars.MOD_ID, "gui");

  public HotbarInvState hotbarInventory;

  public HotbarScreenHandler(int syncId, PlayerInventory playerInventory) {
    this(syncId, playerInventory, new HotbarInvState());
  }

  public HotbarScreenHandler(int syncId, PlayerInventory playerInventory, HotbarInvState inventory) {
    super(ActualHotbarsForge.HOTBAR_SCREEN.get(), syncId);
    this.hotbarInventory = inventory;
    inventory.onOpen(playerInventory.player);
    int rows = inventory.getRowCount();
    int i = (rows - 4) * 18;

    for (int j = 0; j < rows; ++j) {
      for (int k = 0; k < 9; ++k) {
        this.addSlot(new Slot(inventory, k + j * 9, 8 + k * 18, 18 + j * 18));
      }
    }

    for (int j = 0; j < 3; ++j) {
      for (int k = 0; k < 9; ++k) {
        this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
      }
    }

    for (int j = 0; j < 9; ++j) {
      this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 161 + i));
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
