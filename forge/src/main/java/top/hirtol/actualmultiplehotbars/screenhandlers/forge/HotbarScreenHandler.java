package top.hirtol.actualmultiplehotbars.screenhandlers.forge;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.hirtol.actualmultiplehotbars.ActualHotbars;
import top.hirtol.actualmultiplehotbars.forge.ActualHotbarsForge;
import top.hirtol.actualmultiplehotbars.inventory.PartialHotbarInventory;

public class HotbarScreenHandler extends GenericContainerScreenHandler {

  private static final Logger logger = LogManager.getLogger(HotbarScreenHandler.class);

  public HotbarScreenHandler(int syncId, PlayerInventory playerInventory) {
    this(syncId, playerInventory, new PartialHotbarInventory());
  }

  public HotbarScreenHandler(int syncId, PlayerInventory playerInventory, PartialHotbarInventory inventory) {
    super(ActualHotbarsForge.HOTBAR_SCREEN.get(), syncId, playerInventory, inventory, inventory.getRowCount());
//    }
  }
}
