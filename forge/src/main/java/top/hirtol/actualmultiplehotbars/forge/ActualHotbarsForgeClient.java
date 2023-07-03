package top.hirtol.actualmultiplehotbars.forge;

import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.ActualHotbarsClient;
import top.hirtol.actualmultiplehotbars.screenhandlers.forge.HotbarScreen;

@OnlyIn(Dist.CLIENT)
public class ActualHotbarsForgeClient {

  private static final Logger logger = LoggerFactory.getLogger(ActualHotbarsForgeClient.class);

  public static void onConstructor() {
    ActualHotbarsClient.init();
  }

  public static void init(final FMLClientSetupEvent event) {
    HandledScreens.register(ActualHotbarsForge.HOTBAR_SCREEN.get(), HotbarScreen::new);
  }

}
