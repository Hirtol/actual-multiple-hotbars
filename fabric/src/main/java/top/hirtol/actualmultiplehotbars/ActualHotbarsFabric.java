package top.hirtol.actualmultiplehotbars;

import net.fabricmc.api.ModInitializer;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;
import top.hirtol.actualmultiplehotbars.screenhandlers.HotbarScreenHandler;

public class ActualHotbarsFabric implements ModInitializer {

  public static ScreenHandlerType<HotbarScreenHandler> HOTBAR_SCREEN;

  @Override
  public void onInitialize() {
    ActualHotbars.init();

    HOTBAR_SCREEN =
        Registry.register(Registry.SCREEN_HANDLER, HotbarScreenHandler.HOTBAR_SCREEN_ID, new ScreenHandlerType<>(HotbarScreenHandler::new));
  }
}
