package top.hirtol.actualmultiplehotbars.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import top.hirtol.actualmultiplehotbars.ServerState;
import top.hirtol.actualmultiplehotbars.networking.packets.HotbarInvS2CPacket;
import top.hirtol.actualmultiplehotbars.screenhandlers.ScreenHandlers;

public class HotbarInventory extends PartialHotbarInventory {

  private final ServerPlayerEntity player;

  public HotbarInventory(DefaultedList<ItemStack> items, ServerPlayerEntity player) {
    super(items);
    this.player = player;
  }

  public HotbarInventory(PartialHotbarInventory inventory, ServerPlayerEntity player) {
    this(inventory.getItems(), player);
  }

  @Override
  public void markDirty() {
    if (!this.player.world.isClient) {
      var state = ServerState.getServerState(this.player.getServer());
      state.markDirty();
      
      var packet = new HotbarInvS2CPacket(this);
      packet.send(this.player);
    }
  }

  public void openHandledScreen(PlayerEntity player) {
    if (!player.world.isClient) {
      var newScreen =
          new SimpleNamedScreenHandlerFactory((syncId, inv, openPlayer) -> ScreenHandlers.createScreen(syncId, inv, this), Text.translatable("screen.actualmultiplehotbars.ui.title"));

      player.openHandledScreen(newScreen);
    }
  }

  public ServerPlayerEntity getPlayer() {
    return player;
  }
}
