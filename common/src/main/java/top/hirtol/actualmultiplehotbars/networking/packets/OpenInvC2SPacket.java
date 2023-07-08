package top.hirtol.actualmultiplehotbars.networking.packets;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.ActualHotbars;
import top.hirtol.actualmultiplehotbars.inventory.ServerInventoryManager;
import top.hirtol.actualmultiplehotbars.inventory.ServerHotbarInventory;
import top.hirtol.actualmultiplehotbars.networking.C2SPacket;

public class OpenInvC2SPacket implements C2SPacket {

  public static final Identifier ID = new Identifier(ActualHotbars.MOD_ID, "special_action");

  private static final Logger logger = LoggerFactory.getLogger(OpenInvC2SPacket.class);

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
    ServerHotbarInventory playerState = ServerInventoryManager.getPlayerState(serverPlayer);

    playerState.openHandledScreen(serverPlayer);
  }
}
