package top.hirtol.actualmultiplehotbars.screenhandlers;

import static top.hirtol.actualmultiplehotbars.screenhandlers.HotbarScreenHandler.HOTBAR_SCREEN_ID;

import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.MenuRegistry;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.hirtol.actualmultiplehotbars.ActualHotbars;
import top.hirtol.actualmultiplehotbars.inventory.ServerHotbarInventory;

public class ScreenHandlers {

  private static final Logger logger = LogManager.getLogger(ScreenHandlers.class);

  private static final DeferredRegister<ScreenHandlerType<?>> SCREEN_HANDLERS =
      DeferredRegister.create(ActualHotbars.MOD_ID, Registry.MENU_KEY);

  public static RegistrySupplier<ScreenHandlerType<HotbarScreenHandler>> HOTBAR_SCREEN =
      SCREEN_HANDLERS.register(HOTBAR_SCREEN_ID, () -> MenuRegistry.of(HotbarScreenHandler::new));


  public static void init() {
    SCREEN_HANDLERS.register();
  }

  @Environment(EnvType.CLIENT)
  public static void initClient() {
    MenuRegistry.registerScreenFactory(ScreenHandlers.HOTBAR_SCREEN.get(), HotbarScreen::new);
  }

  public static ScreenHandler createHotbarScreen(int syncId, PlayerInventory inv, ServerHotbarInventory hotbarInventory) {
    return new HotbarScreenHandler(syncId, inv, hotbarInventory);
  }
}
