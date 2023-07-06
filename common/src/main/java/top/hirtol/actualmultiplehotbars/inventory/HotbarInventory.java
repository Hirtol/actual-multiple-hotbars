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

  public void linkVisualToVirtualHotbar(int fromVisual, int toVirtual) {
    if (toVirtual < 0 || toVirtual > this.getRowCount() || fromVisual < 0
        || fromVisual > this.getRowCount()) {
      logger.warn("Player {} attempted to use invalid row swap index: `{}` - `{}`", player.getDisplayName().getString(),
          fromVisual, toVirtual);
      return;
    }

    PlayerHotbarState state = this.getVirtualState();
    int fromVirtual = state.visualVirtualMappings.getInt(fromVisual);

    state.visualVirtualMappings.set(fromVisual, toVirtual);

    if (fromVisual == PlayerHotbarState.MAIN_HOTBAR_INDEX || toVirtual == PlayerHotbarState.MAIN_HOTBAR_INDEX) {
      this.swapVirtual(fromVirtual, toVirtual);
    }
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

  public void rotateVisualHotbars(int maxVisualIndexIncl) {
    if (maxVisualIndexIncl < 0 || maxVisualIndexIncl > this.getRowCount()) {
      logger.warn("Player `{}` attempted to rotate invalid hotbar `{}`", this.player.getName().getString(), maxVisualIndexIncl);
      return;
    }

    PlayerHotbarState state = this.getVirtualState();
    int fromVisual = PlayerHotbarState.MAIN_HOTBAR_INDEX;
    int toVisual = maxVisualIndexIncl;

    int previous = state.visualVirtualMappings.getInt(PlayerHotbarState.MAIN_HOTBAR_INDEX);
    for (int i = 0; i < maxVisualIndexIncl; i++) {
      int nextIndex = (state.visualVirtualMappings.getInt((i + 1) % (maxVisualIndexIncl + 1)));
      state.visualVirtualMappings.set(i, nextIndex);
    }

    state.visualVirtualMappings.set(toVisual, previous);

    int fromVirtual = state.visualVirtualMappings.getInt(fromVisual);
    int toVirtual = state.visualVirtualMappings.getInt(toVisual);

    if (fromVirtual == toVirtual) {
      // Possible when a player has assigned two visual hotbars to the same virtual one.
      // In this case we should still mark the state as dirty as the bars *will* move visually, just not physically.
      this.markDirty();
    } else {
      this.swapVirtual(fromVirtual, toVirtual);
    }
  }

  public void resetMappingStates() {
    this.getVirtualState().reset();
    this.markDirty();
  }
}
