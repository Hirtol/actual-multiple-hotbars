package top.hirtol.actualmultiplehotbars.networking;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public interface S2CPacket extends BasePacket {

  /**
   * Handle the current packet on the client
   *
   * @param client The current client
   */
  @Environment(EnvType.CLIENT)
  void handle(MinecraftClient client);

  /**
   * Send this packet to the provided recipient
   *
   * @param recipient The relevant client
   */
  default void send(ServerPlayerEntity recipient) {
    PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
    this.write(buf);
    NetworkManager.sendToPlayer(recipient, this.getId(), buf);
  }

  /**
   * Send the current packet to all players connected to the server
   *
   * @param server The current server
   */
  default void sendAll(MinecraftServer server) {
    PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
    this.write(buf);
    NetworkManager.sendToPlayers(server.getPlayerManager().getPlayerList(), this.getId(), buf);
  }
}
