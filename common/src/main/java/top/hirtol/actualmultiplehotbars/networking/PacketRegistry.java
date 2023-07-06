package top.hirtol.actualmultiplehotbars.networking;

import java.util.Collection;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.networking.packets.HotbarInvS2CPacket;
import top.hirtol.actualmultiplehotbars.networking.packets.HotbarRotateC2SPacket;
import top.hirtol.actualmultiplehotbars.networking.packets.HotbarSetVirtualC2SPacket;
import top.hirtol.actualmultiplehotbars.networking.packets.OpenInvC2SPacket;
import top.hirtol.actualmultiplehotbars.networking.packets.ResetVisualC2SPacket;
import top.hirtol.actualmultiplehotbars.networking.packets.SyncS2CConfigPacket;

public class PacketRegistry {

  private static final Logger logger = LoggerFactory.getLogger(PacketRegistry.class);

  /**
   * Initialise all packet listeners relevant for the server.
   */
  public static void initialiseServer() {
    SimpleNetwork.registerC2S(HotbarSetVirtualC2SPacket.ID, HotbarSetVirtualC2SPacket::read);
    SimpleNetwork.registerC2S(HotbarRotateC2SPacket.ID, HotbarRotateC2SPacket::read);
    SimpleNetwork.registerC2S(OpenInvC2SPacket.ID, OpenInvC2SPacket::read);
    SimpleNetwork.registerC2S(ResetVisualC2SPacket.ID, ResetVisualC2SPacket::read);
  }

  /**
   * Initialise all packet listeners relevant for the client.
   */
  @Environment(EnvType.CLIENT)
  public static void initialiseClient() {
    SimpleNetwork.registerS2C(HotbarInvS2CPacket.ID, HotbarInvS2CPacket::read);
    SimpleNetwork.registerS2C(SyncS2CConfigPacket.ID, SyncS2CConfigPacket::read);
  }

  public static boolean clientSupportsMod(Collection<Identifier> identifiers) {
    return identifiers.contains(HotbarInvS2CPacket.ID);
  }

  public static boolean serverSupportsMod(Collection<Identifier> identifiers) {
    return identifiers.contains(OpenInvC2SPacket.ID);
  }

}
