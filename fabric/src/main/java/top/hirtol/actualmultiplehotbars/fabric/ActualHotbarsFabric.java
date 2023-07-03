package top.hirtol.actualmultiplehotbars.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;
import top.hirtol.actualmultiplehotbars.ActualHotbars;
import top.hirtol.actualmultiplehotbars.screenhandlers.fabric.HotbarScreenHandler;

public class ActualHotbarsFabric implements ModInitializer {

  public static ScreenHandlerType<HotbarScreenHandler> HOTBAR_SCREEN;

  @Override
  public void onInitialize() {
    ActualHotbars.init();

    HOTBAR_SCREEN =
        ScreenHandlerRegistry.registerSimple(HotbarScreenHandler.HOTBAR_SCREEN_ID, HotbarScreenHandler::new);
  }
}
