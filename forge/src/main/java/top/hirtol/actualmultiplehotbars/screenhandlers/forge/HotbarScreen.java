package top.hirtol.actualmultiplehotbars.screenhandlers.forge;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.hirtol.actualmultiplehotbars.client.KeyManager;

public class HotbarScreen extends GenericContainerScreen {

  private static final Logger logger = LogManager.getLogger(HotbarScreen.class);

  public HotbarScreen(GenericContainerScreenHandler handler, PlayerInventory inventory, Text title) {
    super(handler, inventory, title);
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers)
  {
    if(KeyManager.openHotbarKey.matchesKey(keyCode, scanCode))
    {
      ClientPlayerEntity playerEntity = this.client.player;

      if(playerEntity != null)
      {
        this.onClose();
      }
      return true;
    }
    return super.keyPressed(keyCode, scanCode, modifiers);
  }
}
