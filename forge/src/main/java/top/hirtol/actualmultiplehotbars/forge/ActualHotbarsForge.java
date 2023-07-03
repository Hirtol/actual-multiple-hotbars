package top.hirtol.actualmultiplehotbars.forge;

import dev.architectury.platform.forge.EventBuses;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.hirtol.actualmultiplehotbars.ActualHotbars;
import top.hirtol.actualmultiplehotbars.ActualHotbarsClient;
import top.hirtol.actualmultiplehotbars.config.AMHConfigData;
import top.hirtol.actualmultiplehotbars.screenhandlers.forge.HotbarScreenHandler;

@Mod(ActualHotbars.MOD_ID)
public class ActualHotbarsForge {

  public static final DeferredRegister<ScreenHandlerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, ActualHotbars.MOD_ID);
  public static RegistryObject<ScreenHandlerType<HotbarScreenHandler>> HOTBAR_SCREEN = CONTAINERS.register("gui", () -> new ScreenHandlerType<>(HotbarScreenHandler::new));

  public ActualHotbarsForge() {
    onConstructor();
  }

  public static void onConstructor() {
    ModLoadingContext context = ModLoadingContext.get();
    IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
    // Submit our event bus to let architectury register our content on the right time
    EventBuses.registerModEventBus(ActualHotbars.MOD_ID, modBus);
    // Common Init
    ActualHotbars.init();

//    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ActualHotbarsForgeClient::init);
    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> {
      return () -> {
        ActualHotbarsForgeClient.onConstructor();
        modBus.addListener(ActualHotbarsForgeClient::init);
      };
    });

    context.registerExtensionPoint(ConfigScreenFactory.class, () -> {
      return new ConfigScreenFactory(
          (minecraftClient, parent) -> AutoConfig.getConfigScreen(AMHConfigData.class, parent).get());
    });

    CONTAINERS.register(modBus);
  }
}
