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

public class HotbarRotateC2SPacket implements C2SPacket {

  public static final Identifier ID = ActualHotbars.ID("external_hotbar_rotate");
  private static final Logger logger = LoggerFactory.getLogger(HotbarRotateC2SPacket.class);

  public int maxRotateIndexIncl;
  public boolean reverse;

  public HotbarRotateC2SPacket(int maxRotateIndexIncl, boolean reverse) {
    this.maxRotateIndexIncl = maxRotateIndexIncl;
    this.reverse = reverse;
  }

  @Override
  public void handle(MinecraftServer server, ServerPlayerEntity serverPlayer) {
    var state = ServerInventoryManager.getPlayerState(serverPlayer);
    state.rotateVisualHotbars(this.maxRotateIndexIncl, this.reverse);
  }

  @Override
  public Identifier getId() {
    return ID;
  }

  @Override
  public void write(PacketByteBuf buf) {
    buf.writeInt(this.maxRotateIndexIncl);
    buf.writeBoolean(this.reverse);
  }

  public static HotbarRotateC2SPacket read(PacketByteBuf buf) {
    return new HotbarRotateC2SPacket(buf.readInt(), buf.readBoolean());
  }

}
