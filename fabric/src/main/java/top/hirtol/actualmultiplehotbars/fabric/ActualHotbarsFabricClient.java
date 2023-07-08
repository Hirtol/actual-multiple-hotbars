package top.hirtol.actualmultiplehotbars.fabric;

import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.hirtol.actualmultiplehotbars.ActualHotbarsClient;

public class ActualHotbarsFabricClient implements ClientModInitializer {

  private static final Logger logger = LogManager.getLogger(ActualHotbarsFabricClient.class);

  @Override
  public void onInitializeClient() {
    ActualHotbarsClient.init();
    ActualHotbarsClient.initRegistries();

    logger.info("Actual Multiple Hotbars Fabric client has been initialised.");
  }
}
