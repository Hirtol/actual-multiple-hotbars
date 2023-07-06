package top.hirtol.actualmultiplehotbars.networking.packets;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.ActualHotbars;
import top.hirtol.actualmultiplehotbars.ActualHotbars;
import top.hirtol.actualmultiplehotbars.ServerPacketHandler;
import top.hirtol.actualmultiplehotbars.ServerState;
import top.hirtol.actualmultiplehotbars.networking.C2SPacket;

public class HotbarRotateC2SPacket implements C2SPacket {

  public static final Identifier ID = new Identifier(ActualHotbars.MOD_ID, "external_hotbar_rotate");
  private static final Logger logger = LoggerFactory.getLogger(HotbarRotateC2SPacket.class);

  public int maxRotateIndexIncl;

  public HotbarRotateC2SPacket(int maxRotateIndexIncl) {
    this.maxRotateIndexIncl = maxRotateIndexIncl;
  }

  @Override
  public void handle(MinecraftServer server, ServerPlayerEntity serverPlayer) {
    var state = ServerState.getPlayerState(serverPlayer);
    state.rotateVisualHotbars(this.maxRotateIndexIncl);
  }

  @Override
  public Identifier getId() {
    return ID;
  }

  @Override
  public void write(PacketByteBuf buf) {
    buf.writeInt(this.maxRotateIndexIncl);
  }

  public static HotbarRotateC2SPacket read(PacketByteBuf buf) {
    return new HotbarRotateC2SPacket(buf.readInt());
  }

}
