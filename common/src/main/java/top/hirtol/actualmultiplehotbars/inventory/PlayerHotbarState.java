package top.hirtol.actualmultiplehotbars.inventory;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.nbt.NbtCompound;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerHotbarState {

  public static final int MAIN_HOTBAR_INDEX = 0;
  public static final int VANILLA_HOTBAR_SIZE = 9;

  private static final Logger logger = LogManager.getLogger(PlayerHotbarState.class);

  public IntList visualVirtualMappings;
  public IntList virtualPhysicalMappings;

  public PlayerHotbarState(int hotbarCount) {
    this(new IntArrayList(hotbarCount), new IntArrayList(hotbarCount));

    for (int i = 0; i < hotbarCount; i++) {
      this.virtualPhysicalMappings.add(i, i);
      this.visualVirtualMappings.add(i, i);
    }
  }

  public PlayerHotbarState(IntList hotbarMappings, IntList visualMappings) {
    this.virtualPhysicalMappings = hotbarMappings;
    this.visualVirtualMappings = visualMappings;
  }

  public int getPhysicalFromVisual(int visualHotbarIndex) {
    return this.virtualPhysicalMappings.getInt(this.visualVirtualMappings.getInt(visualHotbarIndex));
  }

  public void reset() {
    for (int i = 0; i < this.virtualPhysicalMappings.size(); i++) {
      this.virtualPhysicalMappings.set(i, i);
      this.visualVirtualMappings.set(i, i);
    }
  }

  public void readNbt(NbtCompound nbt) {
    NbtCompound nestedNbt = nbt.getCompound("hotbarState");

    this.virtualPhysicalMappings = IntArrayList.wrap(nestedNbt.getIntArray("hotbarMappings"));
    this.visualVirtualMappings = IntArrayList.wrap(nestedNbt.getIntArray("visualMappings"));
  }

  public NbtCompound writeNbt(NbtCompound nbt) {
    NbtCompound nestedNbt = new NbtCompound();

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
