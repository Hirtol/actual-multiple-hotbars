package top.hirtol.actualmultiplehotbars.networking.packets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.config.AMHConfigData.ServerSettings;
import top.hirtol.actualmultiplehotbars.config.Config;
import top.hirtol.actualmultiplehotbars.ActualHotbars;
import top.hirtol.actualmultiplehotbars.networking.S2CPacket;

public class SyncS2CConfigPacket implements S2CPacket {

  public static final Identifier ID = new Identifier(ActualHotbars.MOD_ID, "config_sync");
  private static final Logger logger = LoggerFactory.getLogger(SyncS2CConfigPacket.class);

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
    var config = new ServerSettings();
    config.additionalHotbars = buf.readInt();
    config.allowFillingAdditional = buf.readBoolean();
    config.preferAdditionalOverMain = buf.readBoolean();
    config.allowFillingEmptyAdditional = buf.readBoolean();

    return new SyncS2CConfigPacket(config);
  }

  @Override
  public void handle(MinecraftClient client) {
    client.execute(() -> Config.getInstance().setRemoteSettings(this.config));
  }
}
