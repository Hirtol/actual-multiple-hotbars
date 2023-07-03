package top.hirtol.actualmultiplehotbars.client;

import java.util.Objects;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import net.minecraft.util.ActionResult;
import org.jetbrains.annotations.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.hirtol.actualmultiplehotbars.config.AMHConfigData;
import top.hirtol.actualmultiplehotbars.config.AMHConfigData.InventoryProvider;
import top.hirtol.actualmultiplehotbars.config.Config;
import top.hirtol.actualmultiplehotbars.client.providers.ExternalHotbarProvider;
import top.hirtol.actualmultiplehotbars.client.providers.HotbarInventoryProvider;
import top.hirtol.actualmultiplehotbars.inventory.PartialHotbarInventory;

public class MultiClientState {

  private static final Logger logger = LogManager.getLogger(MultiClientState.class);
  private static final MultiClientState INSTANCE = new MultiClientState();

  private HotbarInventoryProvider provider;

  @Nullable
  private PartialHotbarInventory hotbarInventory;

  public static MultiClientState getInstance() {
    return INSTANCE;
  }

  public void initialise() {
    AutoConfig.getConfigHolder(AMHConfigData.class).registerSaveListener(this::onConfigSave);

    this.initialiseProvider(this.config().getClientSettings().provider);
  }

  /**
   * Handle invariants that need to be enforced whenever the user config changes.
   */
  private ActionResult onConfigSave(ConfigHolder<AMHConfigData> newConfig, AMHConfigData config) {
    this.initialiseProvider(newConfig.getConfig().client.provider);
    return ActionResult.PASS;
  }

  private void initialiseProvider(InventoryProvider provider) {
    if (provider == InventoryProvider.HotbarInventory) {
      this.provider = new ExternalHotbarProvider();
    }
  }

  public Config config() {
    return Config.getInstance();
  }

  public HotbarInventoryProvider getProvider() {
    return provider;
  }

  public PartialHotbarInventory getHotbarInventory() {
    return hotbarInventory;
  }

  public void setHotbarInventory(PartialHotbarInventory hotbarInventory) {
    this.hotbarInventory = hotbarInventory;
  }
}
