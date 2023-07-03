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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.client.KeyManager;

public class HotbarScreen extends GenericContainerScreen {

  private static final Logger logger = LoggerFactory.getLogger(HotbarScreen.class);

  private static final Identifier TEXTURE = new Identifier("textures/gui/container/shulker_box.png");

//  private final int imageWidth;
//  private final int imageHeight;
//  private final int textureXSize;
//  private final int textureYSize;


  public HotbarScreen(GenericContainerScreenHandler handler, PlayerInventory inventory, Text title) {
    super(handler, inventory, title);
//    this.imageHeight = 222;
//    this.imageWidth = 184;
//    this.textureXSize = 256;
//    this.textureYSize = 256;
  }

//
//  @Override
//  protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
//    RenderSystem.setShader(GameRenderer::getPositionTexShader);
//    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//    RenderSystem.setShaderTexture(0, new Identifier("textures/gui/container/shulker_box.png"));
//
//    int x = (this.width - this.imageWidth) / 2;
//    int y = (this.height - this.imageHeight) / 2;
//
//    drawTexture(matrices, x, y, 0, 0, this.imageWidth, this.imageHeight, this.textureXSize, this.textureYSize);
//  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers)
  {
    if(KeyManager.openHotbarKey.matchesKey(keyCode, scanCode))
    {
      ClientPlayerEntity playerEntity = this.client.player;

      if(playerEntity != null)
      {
        this.close();
      }
      return true;
    }
    return super.keyPressed(keyCode, scanCode, modifiers);
  }
}
