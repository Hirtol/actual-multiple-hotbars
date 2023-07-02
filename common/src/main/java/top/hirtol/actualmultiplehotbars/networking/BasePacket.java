package top.hirtol.actualmultiplehotbars.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public interface BasePacket {

  /**
   * @return The unique packet ID
   */
  Identifier getId();

  void write(PacketByteBuf buf);
}
