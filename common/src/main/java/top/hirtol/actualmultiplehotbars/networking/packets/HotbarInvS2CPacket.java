package top.hirtol.actualmultiplehotbars.networking.packets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.ActualHotbars;
import top.hirtol.actualmultiplehotbars.client.MultiClientState;
import top.hirtol.actualmultiplehotbars.inventory.PartialHotbarInventory;
import top.hirtol.actualmultiplehotbars.networking.S2CPacket;

public class HotbarInvS2CPacket implements S2CPacket {

  public static final Identifier ID = new Identifier(ActualHotbars.MOD_ID, "hotbar_sync");
  private static final Logger logger = LoggerFactory.getLogger(HotbarInvS2CPacket.class);

  private PartialHotbarInventory inventory;

  public HotbarInvS2CPacket(PartialHotbarInventory inventory) {
    this.inventory = inventory;
  }

  @Override
  public void handle(MinecraftClient client) {
    client.executeSync(
        () -> MultiClientState.getInstance().setHotbarInventory(this.inventory));
  }

  @Override
  public Identifier getId() {
    return ID;
  }

  @Override
  public void write(PacketByteBuf buf) {
    buf.writeCollection(inventory.getItems(), PacketByteBuf::writeItemStack);
  }

  public static HotbarInvS2CPacket read(PacketByteBuf buf) {
    var items =
        buf.readList(PacketByteBuf::readItemStack);
    var defaulted = DefaultedList.copyOf(ItemStack.EMPTY, items.toArray(items.toArray(new ItemStack[0])));
    return new HotbarInvS2CPacket(new PartialHotbarInventory(defaulted));
  }

  public PartialHotbarInventory getInventory() {
    return inventory;
  }
}
