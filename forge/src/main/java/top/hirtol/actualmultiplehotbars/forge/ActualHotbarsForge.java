package top.hirtol.actualmultiplehotbars.forge;

import me.shedaniel.architectury.platform.forge.EventBuses;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
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
    IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
    EventBuses.registerModEventBus(ActualHotbars.MOD_ID, modBus);

    // Common Init
    ActualHotbars.init();

    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
      ActualHotbarsForgeClient.onConstructor();
      modBus.addListener(ActualHotbarsForgeClient::init);
    });

    context.registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () ->
        (minecraftClient, parent) -> AutoConfig.getConfigScreen(AMHConfigData.class, parent).get());
  }
}
