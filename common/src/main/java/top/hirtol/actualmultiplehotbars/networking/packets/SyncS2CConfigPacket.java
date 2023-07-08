package top.hirtol.actualmultiplehotbars.networking.packets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.hirtol.actualmultiplehotbars.config.AMHConfigData.ServerSettings;
import top.hirtol.actualmultiplehotbars.config.AMHConfig;
import top.hirtol.actualmultiplehotbars.ActualHotbars;
import top.hirtol.actualmultiplehotbars.networking.S2CPacket;

public class SyncS2CConfigPacket implements S2CPacket {

  public static final Identifier ID = ActualHotbars.ID("config_sync");
  private static final Logger logger = LogManager.getLogger(SyncS2CConfigPacket.class);

  private final ServerSettings config;

  public SyncS2CConfigPacket(ServerSettings config) {
    this.config = config;
  }

  @Override
  public Identifier getId() {
    return ID;
  }

  @Override
  public void write(PacketByteBuf buf) {
    buf.writeInt(this.config.additionalHotbars);
    buf.writeBoolean(this.config.allowFillingAdditional);
    buf.writeBoolean(this.config.preferAdditionalOverMain);
    buf.writeBoolean(this.config.allowFillingEmptyAdditional);
  }

  public static SyncS2CConfigPacket read(PacketByteBuf buf) {
    ServerSettings config = new ServerSettings();
    config.additionalHotbars = buf.readInt();
    config.allowFillingAdditional = buf.readBoolean();
    config.preferAdditionalOverMain = buf.readBoolean();
    config.allowFillingEmptyAdditional = buf.readBoolean();

    return new SyncS2CConfigPacket(config);
  }

  @Override
  public void handle(MinecraftClient client) {
    AMHConfig.getInstance().setRemoteSettings(this.config);
  }
}
