package top.hirtol.actualmultiplehotbars.screenhandlers;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.hirtol.actualmultiplehotbars.inventory.HotbarInventory;

public class ScreenHandlers {

  private static final Logger logger = LogManager.getLogger(ScreenHandlers.class);


  @ExpectPlatform
  public static ScreenHandler createScreen(int syncId, PlayerInventory inv, HotbarInventory hotbarInventory) {
    throw new RuntimeException();
  }
}
