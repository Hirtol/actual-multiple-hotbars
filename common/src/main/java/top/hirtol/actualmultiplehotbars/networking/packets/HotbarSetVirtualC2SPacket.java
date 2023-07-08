package top.hirtol.actualmultiplehotbars.networking.packets;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.ActualHotbars;
import top.hirtol.actualmultiplehotbars.inventory.ServerInventoryManager;
import top.hirtol.actualmultiplehotbars.networking.C2SPacket;

public class HotbarSetVirtualC2SPacket implements C2SPacket {

  public static final Identifier ID = ActualHotbars.ID("external_hotbar_swap");
  private static final Logger logger = LoggerFactory.getLogger(HotbarSetVirtualC2SPacket.class);

  public int virtualIndex;
  public int visualIndex;

  /**
   * Link the given visual hotbar with the behind-the-scenes virtual hotbar. If the `visualIndex` equals
   * {@code MAIN_HOTBAR_INDEX} then the physical hotbars are swapped so that the player holds the items of the new
   * virtual hotbar.
   *
   * @param visualIndex  0 represents the main hotbar
   * @param virtualIndex 0 represents the initial main hotbar's contents.
   */
  public HotbarSetVirtualC2SPacket(int visualIndex, int virtualIndex) {
    this.visualIndex = visualIndex;
    this.virtualIndex = virtualIndex;
  }

  @Override
  public void handle(MinecraftServer server, ServerPlayerEntity serverPlayer) {
    var state = ServerInventoryManager.getPlayerState(serverPlayer);
    state.linkVisualToVirtualHotbar(this.visualIndex, this.virtualIndex);
  }

  @Override
  public Identifier getId() {
    return ID;
  }

  @Override
  public void write(PacketByteBuf buf) {
    buf.writeInt(this.visualIndex);
    buf.writeInt(this.virtualIndex);
  }

  public static HotbarSetVirtualC2SPacket read(PacketByteBuf buf) {
    return new HotbarSetVirtualC2SPacket(buf.readInt(), buf.readInt());
  }

  public int getVirtualIndex() {
    return virtualIndex;
  }

  public int getVisualIndex() {
    return visualIndex;
  }
}
