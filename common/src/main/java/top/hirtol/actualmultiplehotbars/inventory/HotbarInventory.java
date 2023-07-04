package top.hirtol.actualmultiplehotbars.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.ServerPacketHandler;
import top.hirtol.actualmultiplehotbars.ServerState;
import top.hirtol.actualmultiplehotbars.networking.packets.HotbarInvS2CPacket;
import top.hirtol.actualmultiplehotbars.screenhandlers.ScreenHandlers;

public class HotbarInventory extends HotbarInvState {

  private final ServerPlayerEntity player;

  private static final Logger logger = LoggerFactory.getLogger(PlayerHotbarState.class);

  public HotbarInventory(DefaultedList<ItemStack> items, ServerPlayerEntity player, PlayerHotbarState state) {
    super(items, state);
    this.player = player;
  }

  public HotbarInventory(HotbarInvState inventory, ServerPlayerEntity player) {
    this(inventory.getItems(), player, inventory.getVirtualState());
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

  public void equipVirtualHotbar(int virtualHotbarIndex) {
    if (virtualHotbarIndex < 0 || virtualHotbarIndex > this.getRowCount()) {
      logger.warn("Player `{}` attempted to equip invalid hotbar `{}`", this.player.getName().getString(), virtualHotbarIndex);
      return;
    }

    this.swapVirtual(this.getVirtualState().currentVirtualHotbar, virtualHotbarIndex);
  }

  public void swapVirtual(int fromVirtual, int toVirtual) {
    if (toVirtual < 0 || toVirtual > this.getRowCount() || fromVirtual < 0
        || fromVirtual > this.getRowCount()) {
      logger.warn("Player {} attempted to use invalid row swap index: `{}` - `{}`", player.getDisplayName().getString(),
          fromVirtual, toVirtual);
      return;
    }

    PlayerHotbarState state = this.getVirtualState();
    int fromPhysical = state.virtualPhysicalMappings.getInt(fromVirtual);
    int toPhysical = state.virtualPhysicalMappings.getInt(toVirtual);

    state.virtualPhysicalMappings.set(fromVirtual, toPhysical);
    state.virtualPhysicalMappings.set(toVirtual, fromPhysical);
    state.currentVirtualHotbar = toVirtual;

    ServerPacketHandler.swapRow(this.player, fromPhysical, toPhysical);
  }

  public void rotateVirtualHotbars(int maxVirtualIndexExcl) {
    if (maxVirtualIndexExcl < 0 || maxVirtualIndexExcl > this.getRowCount()) {
      logger.warn("Player `{}` attempted to rotate invalid hotbar `{}`", this.player.getName().getString(), maxVirtualIndexExcl);
      return;
    }

    PlayerHotbarState state = this.getVirtualState();

    int fromVirtual = PlayerHotbarState.MAIN_HOTBAR_INDEX;
    int toVirtual = Math.max(maxVirtualIndexExcl - 1, PlayerHotbarState.MAIN_HOTBAR_INDEX);

    this.swapVirtual(fromVirtual, toVirtual);

    for (int i = 0; i < maxVirtualIndexExcl; i++) {
//      state.virtualPhysicalMappings.set(i, (state.virtualPhysicalMappings.getInt(i) + 1) % maxVirtualIndexExcl);
    }

//    ServerPacketHandler.rotateRow(this.player, maxVirtualIndexExcl);
  }
}
