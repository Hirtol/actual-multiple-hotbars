package top.hirtol.actualmultiplehotbars;

import me.shedaniel.architectury.event.events.PlayerEvent;
import net.minecraft.util.ActionResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.hirtol.actualmultiplehotbars.config.AMHConfigData.ServerSettings;
import top.hirtol.actualmultiplehotbars.config.Config;
import top.hirtol.actualmultiplehotbars.inventory.HotbarInventory;
import top.hirtol.actualmultiplehotbars.networking.PacketRegistry;
import top.hirtol.actualmultiplehotbars.networking.packets.HotbarInvS2CPacket;
import top.hirtol.actualmultiplehotbars.networking.packets.SyncS2CConfigPacket;

public class ActualHotbars {

  public static final String MOD_ID = "actualmultiplehotbars";
  public static final Logger logger = LogManager.getLogger(ActualHotbars.class);

  public static void init() {
    Config.init();
    PlayerEvent.PLAYER_JOIN.register(player -> {
      // Keep config in-sync. WARNING -> This currently leaks memory, has to be a better way of doing this
      Config.onChange((configHolder, ConfigData) -> {
        SyncS2CConfigPacket packet = new SyncS2CConfigPacket(ConfigData.serverSettings);
        packet.send(player);
        return ActionResult.PASS;
      });

      // Sync initial state.
      HotbarInventory playerState = ServerState.getPlayerState(player);
      ServerSettings serverSettings = Config.getInstance().getServerSettings();
      SyncS2CConfigPacket syncPacket = new SyncS2CConfigPacket(serverSettings);
      HotbarInvS2CPacket packet = new HotbarInvS2CPacket(playerState);

      syncPacket.send(player);
      packet.send(player);
    });

    PacketRegistry.initialiseServer();

    logger.info("Actual Multiple Hotbars has been initialised");
  }
}
