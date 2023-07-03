package top.hirtol.actualmultiplehotbars.forge;

import dev.architectury.platform.forge.EventBuses;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.hirtol.actualmultiplehotbars.ActualHotbars;
import top.hirtol.actualmultiplehotbars.config.AMHConfigData;

@Mod(ActualHotbars.MOD_ID)
public class ActualHotbarsForge {

  public ActualHotbarsForge() {
    onConstructor();
  }

  public static void onConstructor() {
    ModLoadingContext context = ModLoadingContext.get();

    // Submit our event bus to let architectury register our content on the right time
    EventBuses.registerModEventBus(ActualHotbars.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
    // Common Init
    ActualHotbars.init();

    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ActualHotbarsForgeClient::init);

    context.registerExtensionPoint(ConfigScreenFactory.class, () -> {
      return new ConfigScreenFactory(
          (minecraftClient, parent) -> AutoConfig.getConfigScreen(AMHConfigData.class, parent).get());
    });
  }


}
