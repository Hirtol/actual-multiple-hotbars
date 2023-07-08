package top.hirtol.actualmultiplehotbars.networking.packets;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.hirtol.actualmultiplehotbars.ActualHotbars;
import top.hirtol.actualmultiplehotbars.client.AMHClientState;
import top.hirtol.actualmultiplehotbars.inventory.HotbarInventory;
import top.hirtol.actualmultiplehotbars.inventory.PlayerHotbarState;
import top.hirtol.actualmultiplehotbars.networking.S2CPacket;

public class HotbarInvS2CPacket implements S2CPacket {

  public static final Identifier ID = new Identifier(ActualHotbars.MOD_ID, "hotbar_sync");
  private static final Logger logger = LogManager.getLogger(HotbarInvS2CPacket.class);

  private HotbarInventory inventory;

  public HotbarInvS2CPacket(HotbarInventory inventory) {
    this.inventory = inventory;
  }

  @Override
  public void handle(MinecraftClient client) {
        AMHClientState.getInstance().setHotbarInventory(this.inventory);
  }

  @Override
  public Identifier getId() {
    return ID;
  }

  @Override
  public void write(PacketByteBuf buf) {
    buf.writeIntArray(inventory.getVirtualState().virtualPhysicalMappings.toIntArray());
    buf.writeIntArray(inventory.getVirtualState().visualVirtualMappings.toIntArray());

    buf.writeInt(inventory.getItems().size());
    for (ItemStack item : inventory.getItems()) {
      buf.writeItemStack(item);
    }
  }

  public static HotbarInvS2CPacket read(PacketByteBuf buf) {
    IntList virtualPhysicalMappings = IntArrayList.wrap(buf.readIntArray());
    IntList visualVirtualMappings = IntArrayList.wrap(buf.readIntArray());

    int length = buf.readInt();
    DefaultedList<ItemStack> items = DefaultedList.ofSize(length, ItemStack.EMPTY);
    for (int i = 0; i < length; i++) {
      items.set(i, buf.readItemStack());
    }

    return new HotbarInvS2CPacket(new HotbarInventory(items, new PlayerHotbarState(virtualPhysicalMappings, visualVirtualMappings)));
  }

  public HotbarInventory getInventory() {
    return inventory;
  }
}
