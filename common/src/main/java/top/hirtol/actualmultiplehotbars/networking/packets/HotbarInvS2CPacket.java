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
    client.execute(
        () -> MultiClientState.getInstance().setHotbarInventory(this.inventory));
  }

  @Override
  public Identifier getId() {
    return ID;
  }

  @Override
  public void write(PacketByteBuf buf) {
    buf.writeInt(inventory.getItems().size());
    for (ItemStack item : inventory.getItems()) {
      buf.writeItemStack(item);
    }
  }

  public static HotbarInvS2CPacket read(PacketByteBuf buf) {
    var length = buf.readInt();
    var defaulted = DefaultedList.ofSize(length, ItemStack.EMPTY);
    for (int i = 0; i < length; i++) {
      defaulted.set(i, buf.readItemStack());
    }
    return new HotbarInvS2CPacket(new PartialHotbarInventory(defaulted));
  }

  public PartialHotbarInventory getInventory() {
    return inventory;
  }
}
