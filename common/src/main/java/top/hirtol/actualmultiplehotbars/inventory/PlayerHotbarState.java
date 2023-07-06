package top.hirtol.actualmultiplehotbars.inventory;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.nbt.NbtCompound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerHotbarState {

  public static final int MAIN_HOTBAR_INDEX = 0;
  private static final Logger logger = LoggerFactory.getLogger(PlayerHotbarState.class);


  public IntList visualVirtualMappings;
  public IntList virtualPhysicalMappings;

  /**
   * The index of the currently virtual hotbar that is equipped by the player.
   * <p>
   * Index 0 is the default MC hotbar.
   */
  public int currentVirtualHotbar = MAIN_HOTBAR_INDEX;

  public PlayerHotbarState(int hotbarCount) {
    this(0, new IntArrayList(hotbarCount), new IntArrayList(hotbarCount));

    for (int i = 0; i < hotbarCount; i++) {
      this.virtualPhysicalMappings.add(i, i);
      this.visualVirtualMappings.add(i, i);
    }
  }

  public PlayerHotbarState(int currentVirtualHotbar, IntList hotbarMappings, IntList visualMappings) {
    this.currentVirtualHotbar = currentVirtualHotbar;
    this.virtualPhysicalMappings = hotbarMappings;
    this.visualVirtualMappings = visualMappings;
  }

  public int getFromPhysical(int physicalHotbarIndex) {
    return this.virtualPhysicalMappings.indexOf(physicalHotbarIndex);
  }

  public int getPhysicalFromVisual(int visualHotbarIndex) {
    return this.virtualPhysicalMappings.getInt(this.visualVirtualMappings.getInt(visualHotbarIndex));
  }

  public int getVisualFromVirtual(int virtualHotbarIndex) {
    return this.visualVirtualMappings.indexOf(virtualHotbarIndex);
  }

  public void reset() {
    for (int i = 0; i < this.virtualPhysicalMappings.size(); i++) {
      this.virtualPhysicalMappings.set(i, i);
      this.visualVirtualMappings.set(i, i);
    }
    this.currentVirtualHotbar = 0;
  }

  public void readNbt(NbtCompound nbt) {
    NbtCompound nestedNbt = nbt.getCompound("hotbarState");

    this.currentVirtualHotbar = nestedNbt.getInt("currentVirtualIndex");

    this.virtualPhysicalMappings = IntList.of(nestedNbt.getIntArray("hotbarMappings"));
    this.visualVirtualMappings = IntList.of(nestedNbt.getInt("visualMappings"));
  }

  public NbtCompound writeNbt(NbtCompound nbt) {
    NbtCompound nestedNbt = new NbtCompound();

    nestedNbt.putInt("currentVirtualIndex", this.currentVirtualHotbar);
    nestedNbt.putIntArray("hotbarMappings", this.virtualPhysicalMappings);
    nestedNbt.putIntArray("visualMappings", this.visualVirtualMappings);

    nbt.put("hotbarState", nestedNbt);
    return nbt;
  }

  public static int toInventoryRowIndex(int physicalHotbarIndex) {
    if (physicalHotbarIndex == MAIN_HOTBAR_INDEX) {
      return physicalHotbarIndex;
    } else {
      return physicalHotbarIndex - 1;
    }
  }
}
