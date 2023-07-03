package top.hirtol.actualmultiplehotbars.screenhandlers.forge;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.ActualHotbars;
import top.hirtol.actualmultiplehotbars.forge.ActualHotbarsForge;
import top.hirtol.actualmultiplehotbars.inventory.PartialHotbarInventory;

public class HotbarScreenHandler extends GenericContainerScreenHandler {

  private static final Logger logger = LoggerFactory.getLogger(HotbarScreenHandler.class);
  public static final Identifier HOTBAR_SCREEN_ID = new Identifier(ActualHotbars.MOD_ID, "gui");

  private PartialHotbarInventory hotbarInventory;

  public HotbarScreenHandler(int syncId, PlayerInventory playerInventory) {
    this(syncId, playerInventory, new PartialHotbarInventory());
  }

  public HotbarScreenHandler(int syncId, PlayerInventory playerInventory, PartialHotbarInventory inventory) {
    super(ActualHotbarsForge.HOTBAR_SCREEN.get(), syncId, playerInventory, inventory, inventory.getRowCount());

//    this.hotbarInventory = inventory;
//
//      for (int chestRow = 0; chestRow < inventory.getRowCount(); chestRow++) {
//        for (int chestCol = 0; chestCol < inventory.getColumnCount(); chestCol++) {
//          this.addSlot(new Slot(inventory, chestCol + chestRow * inventory.getColumnCount(), 12 + chestCol * 18, 18 + chestRow * 18));
//        }
//      }
//
//    int i = (this.hotbarInventory.getRowCount() - 4) * 18;
//
//    for(int j = 0; j < 3; ++j) {
//      for(int k = 0; k < 9; ++k) {
//        this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
//      }
//    }
//
//    for(int j = 0; j < 9; ++j) {
//      this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 161 + i));
//    }
  }
//
//  @Override
//  public ItemStack transferSlot(PlayerEntity player, int index) {
//    ItemStack itemStack = ItemStack.EMPTY;
//    Slot slot = this.slots.get(index);
//
//    if (slot.hasStack()) {
//      ItemStack itemStack2 = slot.getStack();
//      itemStack = itemStack2.copy();
//      if (index < this.hotbarInventory.getItems().size()) {
//        if (!this.insertItem(itemStack2, this.hotbarInventory.getItems().size(), this.slots.size(), true)) {
//          return ItemStack.EMPTY;
//        }
//      } else if (!this.insertItem(itemStack2, 0, this.hotbarInventory.getItems().size(), false)) {
//        return ItemStack.EMPTY;
//      }
//
//      if (itemStack2.isEmpty()) {
//        slot.setStack(ItemStack.EMPTY);
//      } else {
//        slot.markDirty();
//      }
//    }
//
//    return itemStack;
//  }
//
//  @Override
//  public boolean canUse(PlayerEntity player) {
//    return true;
//  }
}
