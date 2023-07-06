package top.hirtol.actualmultiplehotbars.networking.packets;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.ActualHotbars;
import top.hirtol.actualmultiplehotbars.ServerState;
import top.hirtol.actualmultiplehotbars.networking.C2SPacket;

public class ResetVisualC2SPacket implements C2SPacket {

  public static final Identifier ID = new Identifier(ActualHotbars.MOD_ID, "reset_visual_hotbar");
  private static final Logger logger = LoggerFactory.getLogger(ResetVisualC2SPacket.class);

  @Override
  public Identifier getId() {
    return ID;
  }

  @Override
  public void write(PacketByteBuf buf) {

  }

  public static ResetVisualC2SPacket read(PacketByteBuf buf) {
    return new ResetVisualC2SPacket();
  }

  @Override
  public void handle(MinecraftServer server, ServerPlayerEntity serverPlayer) {
    var state = ServerState.getPlayerState(serverPlayer);
    state.resetMappingStates();
  }
}
