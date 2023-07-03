package top.hirtol.actualmultiplehotbars.screenhandlers.forge;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.hirtol.actualmultiplehotbars.inventory.HotbarInventory;

public class ScreenHandlersImpl {

  private static final Logger logger = LogManager.getLogger(ScreenHandlersImpl.class);

  public static ScreenHandler createScreen(int syncId, PlayerInventory inv, HotbarInventory hotbarInventory) {
    return new HotbarScreenHandler(syncId, inv, hotbarInventory);
  }
}
