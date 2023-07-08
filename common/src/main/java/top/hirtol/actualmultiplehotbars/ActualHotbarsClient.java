package top.hirtol.actualmultiplehotbars;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.hirtol.actualmultiplehotbars.client.ConfigSyncManager;
import top.hirtol.actualmultiplehotbars.client.KeyManager;
import top.hirtol.actualmultiplehotbars.client.AMHClientState;
import top.hirtol.actualmultiplehotbars.networking.PacketRegistry;
import top.hirtol.actualmultiplehotbars.screenhandlers.ScreenHandlers;

@Environment(EnvType.CLIENT)
public class ActualHotbarsClient {

  private static final Logger logger = LogManager.getLogger(ActualHotbarsClient.class);

  public static void init() {
    AMHClientState.getInstance().initialise();

    ConfigSyncManager.initialise();
    PacketRegistry.initialiseClient();

    KeyManager.initialise();
  }

  public static void initRegistries() {
    ScreenHandlers.initClient();
  }

}
