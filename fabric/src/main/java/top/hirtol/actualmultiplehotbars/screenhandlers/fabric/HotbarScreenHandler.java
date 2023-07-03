package top.hirtol.actualmultiplehotbars.screenhandlers.fabric;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.ActualHotbars;
import top.hirtol.actualmultiplehotbars.fabric.ActualHotbarsFabric;
import top.hirtol.actualmultiplehotbars.inventory.PartialHotbarInventory;

public class HotbarScreenHandler extends SyncedGuiDescription {

  public static final Identifier HOTBAR_SCREEN_ID = new Identifier(ActualHotbars.MOD_ID, "gui");

  private static final Logger logger = LoggerFactory.getLogger(HotbarScreenHandler.class);
  private final PartialHotbarInventory hotbarInventory;

  public HotbarScreenHandler(int syncId, PlayerInventory playerInventory) {
    this(syncId, playerInventory, new PartialHotbarInventory());
  }

  public HotbarScreenHandler(int syncId, PlayerInventory playerInventory, PartialHotbarInventory inventory) {
    super(ActualHotbarsFabric.HOTBAR_SCREEN, syncId, playerInventory);

    this.hotbarInventory = inventory;
    // Not really a 'block' inventory, but this makes quick transfer work
    this.blockInventory = inventory;

    WPlainPanel root = new WPlainPanel();
    setRootPanel(root);

    WItemSlot itemSlot;
    int counter = 0;

    for (int j = 0; j < this.hotbarInventory.getRowCount(); j++) {
      for (int i = 0; i < this.hotbarInventory.getColumnCount(); i++) {
        itemSlot = WItemSlot.of(hotbarInventory, counter);
        root.add(itemSlot, (18 * i), 12 + (18 * j));
        counter++;
      }
    }

    // Sets the correct GUI Size
    root.setInsets(Insets.ROOT_PANEL);

    int height = 15;
    height += 18 * (3);
    int width = 0;

    if (inventory.getColumnCount() > 9) {
      width = 9 * (inventory.getColumnCount() - 9);
    }

    root.add(this.createPlayerInventoryPanel(), width, height);
    root.validate(this);
  }
}
