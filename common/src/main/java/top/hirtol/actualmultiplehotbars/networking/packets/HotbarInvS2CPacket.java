package top.hirtol.actualmultiplehotbars.networking.packets;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.ActualHotbars;
import top.hirtol.actualmultiplehotbars.client.AMHClientState;
import top.hirtol.actualmultiplehotbars.inventory.HotbarInventory;
import top.hirtol.actualmultiplehotbars.inventory.PlayerHotbarState;
import top.hirtol.actualmultiplehotbars.networking.S2CPacket;

public class HotbarInvS2CPacket implements S2CPacket {

  public static final Identifier ID = ActualHotbars.ID("hotbar_sync");
  private static final Logger logger = LoggerFactory.getLogger(HotbarInvS2CPacket.class);

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
    buf.writeIntList(inventory.getVirtualState().virtualPhysicalMappings);
    buf.writeIntList(inventory.getVirtualState().visualVirtualMappings);
    buf.writeCollection(inventory.getItems(), PacketByteBuf::writeItemStack);
  }

  public static HotbarInvS2CPacket read(PacketByteBuf buf) {
    IntList virtualPhysicalMappings = buf.readIntList();
    IntList visualVirtualMappings = buf.readIntList();
    DefaultedList<ItemStack> items = buf.readCollection(DefaultedList::ofSize, PacketByteBuf::readItemStack);

    return new HotbarInvS2CPacket(
        new HotbarInventory(items, new PlayerHotbarState(virtualPhysicalMappings, visualVirtualMappings)));
  }

  public HotbarInventory getInventory() {
    return inventory;
  }
}
