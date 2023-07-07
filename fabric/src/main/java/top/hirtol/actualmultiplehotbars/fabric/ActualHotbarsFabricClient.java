package top.hirtol.actualmultiplehotbars.fabric;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.ActualHotbarsClient;

public class ActualHotbarsFabricClient implements ClientModInitializer {

  private static final Logger logger = LoggerFactory.getLogger(ActualHotbarsFabricClient.class);

  @Override
  public void onInitializeClient() {
    ActualHotbarsClient.init();
    ActualHotbarsClient.initRegistries();

    logger.info("Actual Multiple Hotbars Fabric client has been initialised.");
  }
}
