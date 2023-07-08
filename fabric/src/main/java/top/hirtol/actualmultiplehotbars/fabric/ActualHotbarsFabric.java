package top.hirtol.actualmultiplehotbars.fabric;

import net.fabricmc.api.ModInitializer;
import top.hirtol.actualmultiplehotbars.ActualHotbars;

public class ActualHotbarsFabric implements ModInitializer {

  @Override
  public void onInitialize() {
    ActualHotbars.init();
  }
}
