package top.hirtol.actualmultiplehotbars.fabric;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.fabric.screenhandlers.HotbarScreenHandler;
import top.hirtol.actualmultiplehotbars.inventory.HotbarInventory;

public class ScreenHandlersImpl {

  private static final Logger logger = LoggerFactory.getLogger(ScreenHandlersImpl.class);

  public static ScreenHandler createScreen(int syncId, PlayerInventory inv, HotbarInventory hotbarInventory) {
    return new HotbarScreenHandler(syncId, inv, hotbarInventory);
  }
}
