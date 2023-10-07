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
  /**
   * Stores the state of the hotbar inventory at the time of the most recent death.
   */
  public HashMap<UUID, NbtCompound> historicInventories = new HashMap<>();

  @Override
  public NbtCompound writeNbt(NbtCompound nbt) {
    // Putting the 'players' hashmap, into the 'nbt' which will be saved.
    NbtCompound playersNbtCompound = createHashNbt(players);
    nbt.put("players", playersNbtCompound);

    NbtCompound historicNbtCompound = new NbtCompound();
    historicInventories.forEach(((uuid, nbtCompound) -> historicNbtCompound.put(uuid.toString(), nbtCompound)));
    nbt.put("historicInventories", historicNbtCompound);

    return nbt;
  }

  public static ServerInventoryManager getManager(MinecraftServer server) {
    PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

    return persistentStateManager.getOrCreate(
        ServerInventoryManager::createFromNbt,
        ServerInventoryManager::new,
        ActualHotbars.MOD_ID);
  }

  public static ServerInventoryManager createFromNbt(NbtCompound tag) {
    ServerInventoryManager serverState = new ServerInventoryManager();

    if (tag.contains("historicInventories")) {
      NbtCompound historicTag = tag.getCompound("historicInventories");
      historicTag.getKeys().forEach(key -> {
        UUID uuid = UUID.fromString(key);
        serverState.historicInventories.put(uuid, historicTag.getCompound(key));
      });
    }

    NbtCompound playersTag = tag.getCompound("players");
    serverState.players = readFromNbt(playersTag);

    return serverState;
  }

  /**
   * Get the player's hotbar inventory if we're currently running on the server.
   * <p>
   * Otherwise, `null` is returned.
   *
   * @param player The player to examine
   * @return {@code null} if not running on the server, otherwise the player's additional hotbars.
   */
  @Nullable
  public static ServerHotbarInventory getPlayerState(PlayerEntity player) {
    if (player.world.getServer() != null) {
      ServerInventoryManager serverState = getManager(player.world.getServer());

      var partial = serverState.players.computeIfAbsent(player.getUuid(), uuid -> new HotbarInventory());

      return new ServerHotbarInventory(partial, (ServerPlayerEntity) player);
    } else {
      return null;
    }
  }

  public static void persistHistoricInventory(PlayerEntity player) {
    if (player.world.getServer() != null) {
      ServerInventoryManager state = getManager(player.world.getServer());
      HotbarInventory partial = state.players.computeIfAbsent(player.getUuid(), uuid -> new HotbarInventory());
      NbtCompound playerStateNbt = new NbtCompound();
      partial.writeNbt(playerStateNbt);

      state.historicInventories.put(player.getUuid(), playerStateNbt);
    }
  }

  public static boolean restoreHistoricInventory(PlayerEntity player) {
    if (player.world.getServer() != null) {
      ServerInventoryManager state = getManager(player.world.getServer());
      HotbarInventory historicInventory = new HotbarInventory();

      if (state.historicInventories.containsKey(player.getUuid())) {
        NbtCompound historic = state.historicInventories.get(player.getUuid());
        historicInventory.readNbt(historic);
        state.players.put(player.getUuid(), historicInventory);
        // Send the new updated state to the player.
        historicInventory.markDirty();

        return true;
      }
    }

    return false;
  }

  private static NbtCompound createHashNbt(HashMap<UUID, HotbarInventory> state) {
    NbtCompound compound = new NbtCompound();

    state.forEach((UUID, playerState) -> {
      NbtCompound playerStateNbt = new NbtCompound();

      playerState.writeNbt(playerStateNbt);

      compound.put(String.valueOf(UUID), playerStateNbt);
    });

    return compound;
  }

  private static HashMap<UUID, HotbarInventory> readFromNbt(NbtCompound tag) {
    HashMap<UUID, HotbarInventory> state = new HashMap<>();

    tag.getKeys().forEach(key -> {
      HotbarInventory playerState = new HotbarInventory();
      playerState.readNbt(tag.getCompound(key));

      UUID uuid = UUID.fromString(key);
      state.put(uuid, playerState);
    });

    return state;
  }
}
