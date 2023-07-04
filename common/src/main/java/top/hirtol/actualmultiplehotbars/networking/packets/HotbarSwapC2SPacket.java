package top.hirtol.actualmultiplehotbars.networking.packets;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.ActualHotbars;
import top.hirtol.actualmultiplehotbars.ServerPacketHandler;
import top.hirtol.actualmultiplehotbars.ServerState;
import top.hirtol.actualmultiplehotbars.networking.C2SPacket;

public class HotbarSwapC2SPacket implements C2SPacket {

  public static final Identifier ID = new Identifier(ActualHotbars.MOD_ID, "external_hotbar_swap");
  private static final Logger logger = LoggerFactory.getLogger(HotbarSwapC2SPacket.class);

  public int toIndex;
  public int fromIndex;

  /**
   * @param fromIndex 0 represents current hotbar contents
   * @param toIndex   0 represents current hotbar contents
   */
  public HotbarSwapC2SPacket(int fromIndex, int toIndex) {
    this.fromIndex = fromIndex;
    this.toIndex = toIndex;
  }

  @Override
  public void handle(MinecraftServer server, ServerPlayerEntity serverPlayer) {
    var state = ServerState.getPlayerState(serverPlayer);
    state.swapVirtual(this.fromIndex, this.toIndex);
  }

  @Override
  public Identifier getId() {
    return ID;
  }

  @Override
  public void write(PacketByteBuf buf) {
    buf.writeInt(this.fromIndex);
    buf.writeInt(this.toIndex);
  }

  public static HotbarSwapC2SPacket read(PacketByteBuf buf) {
    return new HotbarSwapC2SPacket(buf.readInt(), buf.readInt());
  }

  public int getToIndex() {
    return toIndex;
  }

  public int getFromIndex() {
    return fromIndex;
  }
}
