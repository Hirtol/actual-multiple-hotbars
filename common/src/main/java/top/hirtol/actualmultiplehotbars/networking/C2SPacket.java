package top.hirtol.actualmultiplehotbars.networking;

import io.netty.buffer.Unpooled;
import me.shedaniel.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public interface C2SPacket extends BasePacket {

  /**
   * Handle the new packet on the server
   *
   * @param server         The current server
   * @param serverPlayer   The player who sent the packet to the server
   */
  void handle(MinecraftServer server, ServerPlayerEntity serverPlayer);

  /**
   * Send the current packet to the server
   */
  @Environment(EnvType.CLIENT)
  default void send() {
    PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
    this.write(buf);
    NetworkManager.sendToServer(this.getId(), buf);
  }
}
