package top.hirtol.actualmultiplehotbars.screenhandlers.fabric;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import top.hirtol.actualmultiplehotbars.client.KeyManager;

public class HotbarScreen extends CottonInventoryScreen<HotbarScreenHandler> {

  public HotbarScreen(HotbarScreenHandler description, PlayerEntity player,
      Text title) {
    super(description, player, title);
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
