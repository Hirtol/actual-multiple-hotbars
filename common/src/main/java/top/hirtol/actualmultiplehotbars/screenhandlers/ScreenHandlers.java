package top.hirtol.actualmultiplehotbars.screenhandlers;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.inventory.HotbarInventory;

public class ScreenHandlers {

  private static final Logger logger = LoggerFactory.getLogger(ScreenHandlers.class);


  @ExpectPlatform
  public static ScreenHandler createScreen(int syncId, PlayerInventory inv, HotbarInventory hotbarInventory) {
    throw new RuntimeException();
  }
}
