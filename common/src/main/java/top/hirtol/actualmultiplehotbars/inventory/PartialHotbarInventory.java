package top.hirtol.actualmultiplehotbars.inventory;

import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import top.hirtol.actualmultiplehotbars.config.Config;

public class PartialHotbarInventory implements ImplementedInventory {

  private final DefaultedList<ItemStack> items;

  public PartialHotbarInventory() {
    this(DefaultedList.ofSize(Config.getInstance().getAdditionalHotbars() * 9, ItemStack.EMPTY));
  }

  public PartialHotbarInventory(DefaultedList<ItemStack> items) {
    this.items = items;
  }

  public static PartialHotbarInventory fromNbt(NbtCompound nbt) {
    PartialHotbarInventory playerState = new PartialHotbarInventory();
    Inventories.readNbt(nbt, playerState.items);
    return playerState;
  }

  public NbtCompound writeNbt(NbtCompound nbt) {
    Inventories.writeNbt(nbt, this.items);

    return nbt;
  }

  public int getColumnCount() {
    return 9;
  }

  public int getRowCount() {
    return this.items.size() / this.getColumnCount();
  }

  @Override
  public DefaultedList<ItemStack> getItems() {
    return this.items;
  }

  public int addStackTillMax(int slot, ItemStack stack) {
    // Copied from PlayerInventory.java
    int j;
    Item item = stack.getItem();
    int i = stack.getCount();
    ItemStack itemStack = this.getStack(slot);
    if (itemStack.isEmpty()) {
      itemStack = new ItemStack(item, 0);
      if (stack.hasNbt()) {
        itemStack.setNbt(stack.getNbt().copy());
      }
      this.setStack(slot, itemStack);
    }
    if ((j = i) > itemStack.getMaxCount() - itemStack.getCount()) {
      j = itemStack.getMaxCount() - itemStack.getCount();
    }
    if (j > this.getMaxCountPerStack() - itemStack.getCount()) {
      j = this.getMaxCountPerStack() - itemStack.getCount();
    }
    if (j == 0) {
      return i;
    }
    itemStack.increment(j);
    itemStack.setBobbingAnimationTime(5);
    return i -= j;
  }

  public int getOccupiedSlotWithRoomForStack(ItemStack stack) {
    for (int i = 0; i < this.items.size(); ++i) {
      if (!this.canStackAddMore(this.items.get(i), stack)) continue;
      return i;
    }
    return -1;
  }

  private boolean canStackAddMore(ItemStack existingStack, ItemStack stack) {
    return !existingStack.isEmpty() && ItemStack.canCombine(existingStack, stack) && existingStack.isStackable() && existingStack.getCount() < existingStack.getMaxCount() && existingStack.getCount() < this.getMaxCountPerStack();
  }
}
