package top.hirtol.actualmultiplehotbars.fabric;

import top.hirtol.actualmultiplehotbars.ActualHotbars;
import net.fabricmc.api.ModInitializer;

public class ActualHotbarsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ActualHotbars.init();
    }
}
