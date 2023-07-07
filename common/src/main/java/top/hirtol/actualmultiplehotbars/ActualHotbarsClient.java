package top.hirtol.actualmultiplehotbars;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.client.ConfigSyncManager;
import top.hirtol.actualmultiplehotbars.client.KeyManager;
import top.hirtol.actualmultiplehotbars.client.MultiClientState;
import top.hirtol.actualmultiplehotbars.networking.PacketRegistry;
import top.hirtol.actualmultiplehotbars.screenhandlers.ScreenHandlers;

@Environment(EnvType.CLIENT)
public class ActualHotbarsClient {

  private static final Logger logger = LoggerFactory.getLogger(ActualHotbarsClient.class);

  public static void init() {
    MultiClientState.getInstance().initialise();

    PacketRegistry.initialiseClient();
    ConfigSyncManager.initialise();

    KeyManager.initialise();
  }

  public static void initRegistries() {
    ScreenHandlers.initClient();
  }

}
