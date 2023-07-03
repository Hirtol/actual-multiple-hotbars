package top.hirtol.actualmultiplehotbars.forge;

import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.hirtol.actualmultiplehotbars.ActualHotbarsClient;
import top.hirtol.actualmultiplehotbars.screenhandlers.forge.HotbarScreen;

@OnlyIn(Dist.CLIENT)
public class ActualHotbarsForgeClient {

  private static final Logger logger = LogManager.getLogger(ActualHotbarsForgeClient.class);

  public static void onConstructor() {
    ActualHotbarsClient.init();
  }

  public static void init(final FMLClientSetupEvent event) {
    HandledScreens.register(ActualHotbarsForge.HOTBAR_SCREEN.get(), HotbarScreen::new);
  }

}
