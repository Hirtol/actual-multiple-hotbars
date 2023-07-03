package top.hirtol.actualmultiplehotbars.networking.packets;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.hirtol.actualmultiplehotbars.ActualHotbars;
import top.hirtol.actualmultiplehotbars.ServerState;
import top.hirtol.actualmultiplehotbars.inventory.HotbarInventory;
import top.hirtol.actualmultiplehotbars.networking.C2SPacket;

public class OpenInvC2SPacket implements C2SPacket {

  public static final Identifier ID = new Identifier(ActualHotbars.MOD_ID, "special_action");

  private static final Logger logger = LogManager.getLogger(OpenInvC2SPacket.class);

  @Override
  public Identifier getId() {
    return ID;
  }

  @Override
  public void write(PacketByteBuf buf) {
  }

  public static OpenInvC2SPacket read(PacketByteBuf buf) {
    return new OpenInvC2SPacket();
  }

  @Override
  public void handle(MinecraftServer server, ServerPlayerEntity serverPlayer) {
    HotbarInventory playerState = ServerState.getPlayerState(serverPlayer);

    playerState.openHandledScreen(serverPlayer);
  }
}
