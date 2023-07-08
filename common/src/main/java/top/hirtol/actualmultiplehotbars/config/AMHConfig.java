package top.hirtol.actualmultiplehotbars.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.event.ConfigSerializeEvent.Save;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.hirtol.actualmultiplehotbars.config.AMHConfigData.ClientSettings;
import top.hirtol.actualmultiplehotbars.config.AMHConfigData.ServerSettings;

public class AMHConfig {

  private static final Logger logger = LogManager.getLogger(AMHConfig.class);

  private static AMHConfig INSTANCE;

  private final AMHConfigData data;
  private ServerSettings remoteSettings;

  public static void init() {
    ConfigHolder<AMHConfigData> data = AMHConfigData.init();

    INSTANCE = new AMHConfig(data.getConfig());
  }

  public static void onChange(Save<AMHConfigData> callback) {
    AutoConfig.getConfigHolder(AMHConfigData.class).registerSaveListener(callback);
  }

  public static AMHConfig getInstance() {
    return INSTANCE;
  }

  public AMHConfig(@NotNull AMHConfigData data) {
    this.data = data;
    this.remoteSettings = null;
  }

  public void setRemoteSettings(@Nullable ServerSettings serverSettings) {
    this.remoteSettings = serverSettings;
    if (serverSettings != null) {
      logger.info("Using server settings: {}", serverSettings.additionalHotbars);
    }
  }

  public int getAdditionalHotbars() {
    if (this.remoteSettings != null) {
      return this.remoteSettings.additionalHotbars;
    } else {
      return this.data.serverSettings.additionalHotbars;
    }
  }

  public boolean allowFillingAdditional() {
    if (this.remoteSettings != null) {
      return this.remoteSettings.allowFillingAdditional;
    } else {
      return this.data.serverSettings.allowFillingAdditional;
    }
  }

  public boolean preferAdditionalOverMain() {
    if (this.remoteSettings != null) {
      return this.remoteSettings.preferAdditionalOverMain;
    } else {
      return this.data.serverSettings.preferAdditionalOverMain;
    }
  }

  public boolean allowFillingEmptyAdditional() {
    if (this.remoteSettings != null) {
      return this.remoteSettings.allowFillingEmptyAdditional;
    } else {
      return this.data.serverSettings.allowFillingEmptyAdditional;
    }
  }

  public ClientSettings getClientSettings() {
    return this.data.client;
  }

  /**
   * Only to be used on the server
   */
  public ServerSettings getServerSettings() {
    return this.data.serverSettings;
  }
}
