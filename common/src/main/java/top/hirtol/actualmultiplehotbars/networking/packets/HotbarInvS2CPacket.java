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
import top.hirtol.actualmultiplehotbars.client.MultiClientState;
import top.hirtol.actualmultiplehotbars.inventory.HotbarInvState;
import top.hirtol.actualmultiplehotbars.inventory.PlayerHotbarState;
import top.hirtol.actualmultiplehotbars.networking.S2CPacket;

public class HotbarInvS2CPacket implements S2CPacket {

  public static final Identifier ID = new Identifier(ActualHotbars.MOD_ID, "hotbar_sync");
  private static final Logger logger = LoggerFactory.getLogger(HotbarInvS2CPacket.class);

  private HotbarInvState inventory;

  public HotbarInvS2CPacket(HotbarInvState inventory) {
    this.inventory = inventory;
  }

  @Override
  public void handle(MinecraftClient client) {
        MultiClientState.getInstance().setHotbarInventory(this.inventory);
  }

  @Override
  public Identifier getId() {
    return ID;
  }

  @Override
  public void write(PacketByteBuf buf) {
    buf.writeInt(inventory.getVirtualState().currentVirtualHotbar);
    buf.writeIntList(inventory.getVirtualState().virtualPhysicalMappings);
    buf.writeIntList(inventory.getVirtualState().visualVirtualMappings);
    buf.writeCollection(inventory.getItems(), PacketByteBuf::writeItemStack);
  }

  public static HotbarInvS2CPacket read(PacketByteBuf buf) {
    int currentVirtualHotbar = buf.readInt();
    IntList virtualPhysicalMappings = buf.readIntList();
    IntList visualVirtualMappings = buf.readIntList();
    DefaultedList<ItemStack> items = buf.readCollection(DefaultedList::ofSize, PacketByteBuf::readItemStack);

    return new HotbarInvS2CPacket(new HotbarInvState(items, new PlayerHotbarState(currentVirtualHotbar, virtualPhysicalMappings, visualVirtualMappings)));
  }

  public HotbarInvState getInventory() {
    return inventory;
  }
}
