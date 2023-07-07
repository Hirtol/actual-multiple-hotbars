package top.hirtol.actualmultiplehotbars.inventory;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.nbt.NbtCompound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerHotbarState {

  public static final int MAIN_HOTBAR_INDEX = 0;
  public static final int VANILLA_HOTBAR_SIZE = 9;

  private static final Logger logger = LoggerFactory.getLogger(PlayerHotbarState.class);


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
  }

  public void readNbt(NbtCompound nbt) {
    NbtCompound nestedNbt = nbt.getCompound("hotbarState");

    this.virtualPhysicalMappings = IntArrayList.of(nestedNbt.getIntArray("hotbarMappings"));
    this.visualVirtualMappings = IntArrayList.of(nestedNbt.getIntArray("visualMappings"));
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
