package top.hirtol.actualmultiplehotbars.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.ActualHotbarsClient;
import top.hirtol.actualmultiplehotbars.screenhandlers.fabric.HotbarScreen;
import top.hirtol.actualmultiplehotbars.screenhandlers.fabric.HotbarScreenHandler;

public class ActualHotbarsFabricClient implements ClientModInitializer {

  private static final Logger logger = LoggerFactory.getLogger(ActualHotbarsFabricClient.class);

  @Override
  public void onInitializeClient() {
    ActualHotbarsClient.init();

    HandledScreens.<HotbarScreenHandler, HotbarScreen>register(ActualHotbarsFabric.HOTBAR_SCREEN,
        (handler, playerInventory, title) -> new HotbarScreen(handler, playerInventory.player, title));

    logger.info("Actual Multiple Hotbars Fabric client has been initialised.");
  }
}
