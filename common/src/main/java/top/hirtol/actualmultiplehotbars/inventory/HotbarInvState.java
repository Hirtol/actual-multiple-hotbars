package top.hirtol.actualmultiplehotbars.inventory;

import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import top.hirtol.actualmultiplehotbars.config.Config;

public class HotbarInvState implements ImplementedInventory {

  private final DefaultedList<ItemStack> items;

  private final PlayerHotbarState state;

  public HotbarInvState() {
    this(DefaultedList.ofSize(Config.getInstance().getAdditionalHotbars() * 9, ItemStack.EMPTY), new PlayerHotbarState(1 + Config.getInstance().getAdditionalHotbars()));
  }

  public HotbarInvState(DefaultedList<ItemStack> items, PlayerHotbarState state) {
    this.items = items;
    this.state = state;
  }

  public void readNbt(NbtCompound nbt) {
    Inventories.readNbt(nbt, this.items);
    state.readNbt(nbt);
  }

  public NbtCompound writeNbt(NbtCompound nbt) {
    Inventories.writeNbt(nbt, this.items);
    state.writeNbt(nbt);

    return nbt;
  }

  public int getColumnCount() {
    return PlayerHotbarState.VANILLA_HOTBAR_SIZE;
  }

  public int getRowCount() {
    return this.items.size() / this.getColumnCount();
  }

  public PlayerHotbarState getVirtualState() {
    return state;
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
    itemStack.setBobbingAnimationTime(0);
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
