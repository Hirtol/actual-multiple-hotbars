package top.hirtol.actualmultiplehotbars.client;

import dev.architectury.event.events.client.ClientPlayerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.config.AMHConfig;

public class ConfigSyncManager {

  private static final Logger logger = LoggerFactory.getLogger(ConfigSyncManager.class);

  public static void initialise() {
    ClientPlayerEvent.CLIENT_PLAYER_QUIT.register((player) -> {
      AMHConfig.getInstance().setRemoteSettings(null);
      logger.info("Reset settings after server disconnect!");
    });
  }

}
