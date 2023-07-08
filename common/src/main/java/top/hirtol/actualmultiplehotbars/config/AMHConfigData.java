package top.hirtol.actualmultiplehotbars.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.ActualHotbars;

@Config(name = ActualHotbars.MOD_ID)
public class AMHConfigData implements ConfigData {

  @ConfigEntry.Gui.Excluded
  private static final Logger logger = LoggerFactory.getLogger(AMHConfigData.class);

  @ConfigEntry.Category("Server")
  @ConfigEntry.Gui.TransitiveObject
  public ServerSettings serverSettings = new ServerSettings();

  @ConfigEntry.Gui.TransitiveObject
  public ClientSettings client = new ClientSettings();

  public static ConfigHolder<AMHConfigData> init() {
    AutoConfig.register(AMHConfigData.class, JanksonConfigSerializer::new);
    return AutoConfig.getConfigHolder(AMHConfigData.class);
  }

  public static class ServerSettings {

    /**
     * The amount of additional hotbars available for players.
     * <p>
     * Server enforced.
     */
    @ConfigEntry.BoundedDiscrete(min = 0, max = 6)
    @ConfigEntry.Gui.RequiresRestart
    @Comment("The amount of additional hotbars available for players when `HotbarInventory` is selected. Server Enforced")
    public int additionalHotbars = 3;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean preferAdditionalOverMain = true;
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean allowFillingAdditional = true;
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean allowFillingEmptyAdditional = false;
  }

  public static class ClientSettings {

    public int shift = 21;

    @ConfigEntry.BoundedDiscrete(min = 0, max = 6)
    @ConfigEntry.Gui.RequiresRestart
    public int additionalVisibleHotbars = 1;

    /**
     * When `true` will allow the 'rotate hotbar' hotkey to rotate into hotbars which are not current visible in
     * sequence.
     */
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean rotateBeyondVisible = false;

    @ConfigEntry.Gui.Tooltip
    public boolean reverseBars = true;

    @ConfigEntry.Gui.Tooltip
    public boolean allowScrollSwap = true;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public ModifierKey swapModifierKey = ModifierKey.Control;

    @ConfigEntry.Category("Provider")
    @ConfigEntry.Gui.Tooltip
    public InventoryProvider provider = InventoryProvider.HotbarInventory;

    public boolean debugMode = false;

    public int totalHotbars() {
      return 1 + this.additionalVisibleHotbars;
    }
  }

  public enum InventoryProvider {
    HotbarInventory,
  }

  public enum ModifierKey {
    Control,
    Shift,
    Alt,
    Disabled;


    public int toGlfwModifier() {
      return switch (this) {
        case Control -> GLFW.GLFW_MOD_CONTROL;
        case Shift -> GLFW.GLFW_MOD_SHIFT;
        case Alt -> GLFW.GLFW_MOD_ALT;
        case Disabled -> 0xFFFFFFF;
      };
    }
  }

  @Override
  public void validatePostLoad() throws ValidationException {
    int maxVisibleHotbars = this.client.provider == InventoryProvider.HotbarInventory ?
        this.serverSettings.additionalHotbars : 3; // Minecraft default inventory size

    if (this.client.additionalVisibleHotbars > maxVisibleHotbars) {
      logger.warn("Attempt to display more additional hotbars than available");
      this.client.additionalVisibleHotbars = maxVisibleHotbars;
    }
  }
}
