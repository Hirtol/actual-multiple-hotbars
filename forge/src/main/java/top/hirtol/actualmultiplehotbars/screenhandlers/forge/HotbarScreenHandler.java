package top.hirtol.actualmultiplehotbars.screenhandlers.forge;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.ActualHotbars;
import top.hirtol.actualmultiplehotbars.forge.ActualHotbarsForge;
import top.hirtol.actualmultiplehotbars.inventory.HotbarInvState;

public class HotbarScreenHandler extends GenericContainerScreenHandler {

  private static final Logger logger = LoggerFactory.getLogger(HotbarScreenHandler.class);
  public static final Identifier HOTBAR_SCREEN_ID = new Identifier(ActualHotbars.MOD_ID, "gui");

  private HotbarInvState hotbarInventory;

  public HotbarScreenHandler(int syncId, PlayerInventory playerInventory) {
    this(syncId, playerInventory, new HotbarInvState());
  }

  public HotbarScreenHandler(int syncId, PlayerInventory playerInventory, HotbarInvState inventory) {
    super(ActualHotbarsForge.HOTBAR_SCREEN.get(), syncId, playerInventory, inventory, inventory.getRowCount());
  }
}
