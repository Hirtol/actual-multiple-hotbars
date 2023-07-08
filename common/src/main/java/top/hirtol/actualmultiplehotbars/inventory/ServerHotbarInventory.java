package top.hirtol.actualmultiplehotbars.inventory;

import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.ServerPacketHandler;
import top.hirtol.actualmultiplehotbars.networking.packets.HotbarInvS2CPacket;
import top.hirtol.actualmultiplehotbars.screenhandlers.ScreenHandlers;

public class ServerHotbarInventory extends HotbarInventory {

  private final ServerPlayerEntity player;

  private static final Logger logger = LoggerFactory.getLogger(PlayerHotbarState.class);

  public ServerHotbarInventory(DefaultedList<ItemStack> items, ServerPlayerEntity player, PlayerHotbarState state) {
    super(items, state);
    this.player = player;
  }

  public ServerHotbarInventory(HotbarInventory inventory, ServerPlayerEntity player) {
    this(inventory.getItems(), player, inventory.getVirtualState());
  }

  @Override
  public void markDirty() {
    if (!this.player.world.isClient) {
      var state = ServerInventoryManager.getManager(this.player.getServer());
      state.markDirty();

      var packet = new HotbarInvS2CPacket(this);
      packet.send(this.player);
    }
  }

  public void openHandledScreen(ServerPlayerEntity player) {
    var newScreen = new SimpleNamedScreenHandlerFactory(
        (syncId, inv, openPlayer) -> ScreenHandlers.createHotbarScreen(syncId, inv, this),
        Text.translatable("screen.actualmultiplehotbars.ui.title"));
    MenuRegistry.openMenu(player, newScreen);
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

    ServerPacketHandler.swapRow(this.player, fromPhysical, toPhysical);
  }

  /**
   * Rotate the given range of hotbars (from visual 0..=maxVisualIndexIncl).
   *
   * Will only shift the physical items of the current equipped hotbar and the hotbar to-be equipped.
   * The remainder are merely visual moves.
   * @param maxVisualIndexIncl The max index of the Visual hotbars to rotate.
   * @param reverse Whether to rotate in reverse or not
   */
  public void rotateVisualHotbars(int maxVisualIndexIncl, boolean reverse) {
    if (maxVisualIndexIncl < 0 || maxVisualIndexIncl > this.getRowCount()) {
      logger.warn("Player `{}` attempted to rotate invalid hotbar `{}`", this.player.getName().getString(),
          maxVisualIndexIncl);
      return;
    }
    if (maxVisualIndexIncl == 0) {
      return;
    }

    PlayerHotbarState state = this.getVirtualState();
    int fromVisual = reverse ? PlayerHotbarState.MAIN_HOTBAR_INDEX + 1 : PlayerHotbarState.MAIN_HOTBAR_INDEX;
    int toVisual = reverse ? PlayerHotbarState.MAIN_HOTBAR_INDEX : maxVisualIndexIncl;

    int previousVirtual;

    if (reverse) {
      previousVirtual = state.visualVirtualMappings.getInt(maxVisualIndexIncl);

      for (int i = maxVisualIndexIncl; i > 0; i--) {
        int previousIndex = state.visualVirtualMappings.getInt(i - 1);
        state.visualVirtualMappings.set(i, previousIndex);
      }

    } else {
      previousVirtual = state.visualVirtualMappings.getInt(PlayerHotbarState.MAIN_HOTBAR_INDEX);

      for (int i = 0; i < maxVisualIndexIncl; i++) {
        int nextIndex = (state.visualVirtualMappings.getInt((i + 1) % (maxVisualIndexIncl + 1)));
        state.visualVirtualMappings.set(i, nextIndex);
      }
    }

    state.visualVirtualMappings.set(toVisual, previousVirtual);

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
