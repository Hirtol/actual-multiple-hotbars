package top.hirtol.actualmultiplehotbars.networking;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.NetworkManager.Side;
import java.util.function.Function;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleNetwork {

  private static final Logger logger = LoggerFactory.getLogger(SimpleNetwork.class);

  public static <T extends C2SPacket> void registerC2S(Identifier id, Function<PacketByteBuf, T> packetRead) {
    NetworkManager.registerReceiver(Side.C2S, id, (buf, context) -> {
      T packet = packetRead.apply(buf);
      context.queue(() -> packet.handle(context.getPlayer().getServer(), (ServerPlayerEntity) context.getPlayer()));
    });
  }

  public static <T extends S2CPacket> void registerS2C(Identifier id, Function<PacketByteBuf, T> packetRead) {
    NetworkManager.registerReceiver(Side.S2C, id, (buf, context) -> {
      T packet = packetRead.apply(buf);
      context.queue(() -> packet.handle(MinecraftClient.getInstance()));
    });
  }
}
