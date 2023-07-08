package top.hirtol.actualmultiplehotbars.inventory;

import java.util.HashMap;
import java.util.UUID;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import top.hirtol.actualmultiplehotbars.ActualHotbars;

public class ServerInventoryManager extends PersistentState {

  public HashMap<UUID, HotbarInventory> players = new HashMap<>();

  public ServerInventoryManager() {
    super(ActualHotbars.MOD_ID);
  }

  @Override
  public void fromTag(NbtCompound tag) {
    NbtCompound playersTag = tag.getCompound("players");
    playersTag.getKeys().forEach(key -> {
      HotbarInventory playerState = new HotbarInventory();
      playerState.readNbt(playersTag.getCompound(key));

      UUID uuid = UUID.fromString(key);
      this.players.put(uuid, playerState);
    });
  }

  @Override
  public NbtCompound writeNbt(NbtCompound nbt) {
    // Putting the 'players' hashmap, into the 'nbt' which will be saved.
    NbtCompound playersNbtCompound = new NbtCompound();
    players.forEach((UUID, playerState) -> {
      NbtCompound playerStateNbt = new NbtCompound();

      playerState.writeNbt(playerStateNbt);

      playersNbtCompound.put(String.valueOf(UUID), playerStateNbt);
    });
    nbt.put("players", playersNbtCompound);

    return nbt;
  }

  public static ServerInventoryManager getManager(MinecraftServer server) {
    PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

    return persistentStateManager.getOrCreate(
        ServerInventoryManager::new,
        ActualHotbars.MOD_ID);
  }

  public static ServerInventoryManager createFromNbt(NbtCompound tag) {
    ServerInventoryManager serverState = new ServerInventoryManager();

    serverState.fromTag(tag);

    return serverState;
  }

  /**
   * Get the player's hotbar inventory if we're currently running on the server.
   *
   * Otherwise, `null` is returned.
   * @param player The player to examine
   * @return {@code null} if not running on the server, otherwise the player's additional hotbars.
   */
  @Nullable
  public static ServerHotbarInventory getPlayerState(PlayerEntity player) {
    if (player.world.getServer() != null) {
      ServerInventoryManager serverState = getManager(player.world.getServer());

      HotbarInventory partial = serverState.players.computeIfAbsent(player.getUuid(), uuid -> new HotbarInventory());

      return new ServerHotbarInventory(partial, (ServerPlayerEntity) player);
    } else {
      return null;
    }
  }
}
