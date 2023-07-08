package top.hirtol.actualmultiplehotbars.client;

import me.shedaniel.architectury.event.events.client.ClientPlayerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.hirtol.actualmultiplehotbars.config.AMHConfig;

public class ConfigSyncManager {

  private static final Logger logger = LogManager.getLogger(ConfigSyncManager.class);

  public static void initialise() {
    ClientPlayerEvent.CLIENT_PLAYER_QUIT.register((player) -> {
      AMHConfig.getInstance().setRemoteSettings(null);
      logger.info("Reset settings after server disconnect!");
    });
  }

}
