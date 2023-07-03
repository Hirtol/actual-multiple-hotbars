package top.hirtol.actualmultiplehotbars;

import net.minecraft.client.gui.screen.ingame.HandledScreens;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.hirtol.actualmultiplehotbars.client.ConfigSyncManager;
import top.hirtol.actualmultiplehotbars.client.KeyManager;
import top.hirtol.actualmultiplehotbars.client.MultiClientState;
import top.hirtol.actualmultiplehotbars.networking.PacketRegistry;

public class ActualHotbarsClient {

  private static final Logger logger = LogManager.getLogger(ActualHotbarsClient.class);

  public static void init() {
    MultiClientState.getInstance().initialise();
    KeyManager.initialise();
    ConfigSyncManager.initialise();
    PacketRegistry.initialiseClient();
  }

}
