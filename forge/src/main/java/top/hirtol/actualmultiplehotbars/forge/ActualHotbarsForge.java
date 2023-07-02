package top.hirtol.actualmultiplehotbars.forge;

import dev.architectury.platform.forge.EventBuses;
import top.hirtol.actualmultiplehotbars.ActualHotbars;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ActualHotbars.MOD_ID)
public class ActualHotbarsForge {
    public ActualHotbarsForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(ActualHotbars.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        ActualHotbars.init();
    }
}
