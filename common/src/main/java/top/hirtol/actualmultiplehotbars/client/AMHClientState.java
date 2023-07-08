package top.hirtol.actualmultiplehotbars.client;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import net.minecraft.util.ActionResult;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.client.providers.ExternalHotbarProvider;
import top.hirtol.actualmultiplehotbars.client.providers.HotbarInventoryProvider;
import top.hirtol.actualmultiplehotbars.config.AMHConfigData;
import top.hirtol.actualmultiplehotbars.config.AMHConfigData.InventoryProvider;
import top.hirtol.actualmultiplehotbars.inventory.HotbarInventory;

public class AMHClientState {

  private static final Logger logger = LoggerFactory.getLogger(AMHClientState.class);
  private static final AMHClientState INSTANCE = new AMHClientState();

  private HotbarInventoryProvider provider;

  @Nullable
  private HotbarInventory hotbarInventory;

  public static AMHClientState getInstance() {
    return INSTANCE;
  }

  public void initialise() {
    ConfigHolder<AMHConfigData> config = AutoConfig.getConfigHolder(AMHConfigData.class);
    config.registerSaveListener(this::onConfigSave);

    this.initialiseProvider(config.getConfig().client.provider);
  }

  /**
   * Handle invariants that need to be enforced whenever the user config changes.
   */
  private ActionResult onConfigSave(ConfigHolder<AMHConfigData> newConfig, AMHConfigData config) {
    this.initialiseProvider(config.client.provider);
    return ActionResult.PASS;
  }

  private void initialiseProvider(InventoryProvider provider) {
    this.provider = switch (provider) {
      case HotbarInventory -> new ExternalHotbarProvider();
    };
  }

  public HotbarInventoryProvider getProvider() {
    return provider;
  }

  @Nullable
  public HotbarInventory getHotbarInventory() {
    return hotbarInventory;
  }

  public void setHotbarInventory(@Nullable HotbarInventory hotbarInventory) {
    this.hotbarInventory = hotbarInventory;
  }
}
