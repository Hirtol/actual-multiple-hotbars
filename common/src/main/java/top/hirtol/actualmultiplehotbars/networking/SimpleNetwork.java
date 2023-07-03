package top.hirtol.actualmultiplehotbars.networking;

import java.util.function.Function;
import me.shedaniel.architectury.networking.NetworkManager;
import me.shedaniel.architectury.networking.NetworkManager.Side;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleNetwork {

  private static final Logger logger = LogManager.getLogger(SimpleNetwork.class);

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
