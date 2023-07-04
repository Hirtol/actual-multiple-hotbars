package top.hirtol.actualmultiplehotbars.inventory;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.nbt.NbtCompound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerHotbarState {

  public static final int MAIN_HOTBAR_INDEX = 0;
  private static final Logger logger = LoggerFactory.getLogger(PlayerHotbarState.class);


  public IntList virtualPhysicalMappings;

  /**
   * The index of the currently virtual hotbar that is equipped by the player.
   * <p>
   * Index 0 is the default MC hotbar.
   */
  public int currentVirtualHotbar = MAIN_HOTBAR_INDEX;

  public PlayerHotbarState(int hotbarCount) {
    this(0, new IntArrayList(hotbarCount));

    for (int i = 0; i < hotbarCount; i++) {
      this.virtualPhysicalMappings.add(i, i);
    }
  }

  public PlayerHotbarState(int currentVirtualHotbar, IntList hotbarMappings) {
    this.currentVirtualHotbar = currentVirtualHotbar;
    this.virtualPhysicalMappings = hotbarMappings;
  }

  public int getFromPhysical(int physicalHotbarIndex) {
    return this.virtualPhysicalMappings.indexOf(physicalHotbarIndex);
  }

  public void readNbt(NbtCompound nbt) {
    NbtCompound nestedNbt = nbt.getCompound("hotbarState");

    this.currentVirtualHotbar = nestedNbt.getInt("currentVirtualIndex");

    this.virtualPhysicalMappings = IntList.of(nestedNbt.getIntArray("hotbarMappings"));
  }

  public NbtCompound writeNbt(NbtCompound nbt) {
    NbtCompound nestedNbt = new NbtCompound();

    nestedNbt.putInt("currentVirtualIndex", this.currentVirtualHotbar);
    nestedNbt.putIntArray("hotbarMappings", this.virtualPhysicalMappings);

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
