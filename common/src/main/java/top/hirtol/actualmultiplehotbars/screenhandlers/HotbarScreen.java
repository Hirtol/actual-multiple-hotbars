package top.hirtol.actualmultiplehotbars.screenhandlers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.ActualHotbars;
import top.hirtol.actualmultiplehotbars.client.AMHClientState;
import top.hirtol.actualmultiplehotbars.client.KeyManager;

// GenericContainerScreen
public class HotbarScreen extends HandledScreen<HotbarScreenHandler>
    implements ScreenHandlerProvider<HotbarScreenHandler> {

  private static final Logger logger = LoggerFactory.getLogger(HotbarScreen.class);
  private static final Identifier TEXTURE = ActualHotbars.ID("textures/gui/josh_container.png");

  private final int rows;

  private final int hotbarWidth;
  private final int hotbarHeight;

  private final int textureOffsetX;
  private final int textureOffsetY;


  public HotbarScreen(HotbarScreenHandler handler, PlayerInventory inventory, Text title) {
    super(handler, inventory, title);
    this.passEvents = false;

    this.textureOffsetX = 5;
    this.textureOffsetY = 5;
    this.hotbarWidth = 166 + this.textureOffsetX;
    this.hotbarHeight = 18;

    this.rows = handler.hotbarInventory.getRowCount();
    this.backgroundHeight = 114 + this.rows * this.hotbarHeight;
    this.playerInventoryTitleY = this.backgroundHeight - 94;
  }

  @Override
  protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
    this.textRenderer.draw(matrices, this.playerInventoryTitle, (float) this.playerInventoryTitleX,
        (float) this.playerInventoryTitleY, 0x404040);
  }

  @Override
  public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
    this.renderBackground(matrices);
    super.render(matrices, mouseX, mouseY, delta);
    this.drawMouseoverTooltip(matrices, mouseX, mouseY);
  }

  @Override
  protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    int i = (this.width - this.backgroundWidth) / 2;
    int j = (this.height - this.backgroundHeight) / 2;

    int heightAddition = 15;
    int currentHotbar =
        AMHClientState.getInstance().getHotbarInventory().getVirtualState().visualVirtualMappings.getInt(0);
    // Draw hotbar identifiers
    for (int k = 0; k < this.rows; k++) {
      int color = (k + 1) == currentHotbar ? 0xD040D0 : 0x804080;

      this.textRenderer.drawWithShadow(matrices, Integer.toString(k + 2), (float) i - 6,
          (j + heightAddition + 8 + (k * this.hotbarHeight)), color);
    }

    RenderSystem.setShaderTexture(0, TEXTURE);
    // Render the player's inventory first
    this.drawTexture(matrices, i, j + this.rows * this.hotbarHeight + 17, 0, 126, this.backgroundWidth, 96);
    // Now draw the hotbar inventories
    this.drawTexture(matrices, i, j + heightAddition, 0, this.textureOffsetY, this.hotbarWidth,
        (this.hotbarHeight * this.rows) + 3);
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (KeyManager.openHotbarKey.matchesKey(keyCode, scanCode)) {
      ClientPlayerEntity playerEntity = this.client.player;

      if (playerEntity != null) {
        this.close();
      }
      return true;
    }
    return super.keyPressed(keyCode, scanCode, modifiers);
  }
}
