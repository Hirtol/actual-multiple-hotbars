package top.hirtol.actualmultiplehotbars;

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
import top.hirtol.actualmultiplehotbars.inventory.HotbarInventory;
import top.hirtol.actualmultiplehotbars.inventory.HotbarInvState;

public class ServerState extends PersistentState {

  public HashMap<UUID, HotbarInvState> players = new HashMap<>();

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

  public static ServerState getServerState(MinecraftServer server) {
    PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

    return persistentStateManager.getOrCreate(
        ServerState::createFromNbt,
        ServerState::new,
        ActualHotbars.MOD_ID);
  }

  public static ServerState createFromNbt(NbtCompound tag) {
    ServerState serverState = new ServerState();

    NbtCompound playersTag = tag.getCompound("players");
    playersTag.getKeys().forEach(key -> {
      HotbarInvState playerState = new HotbarInvState();
      playerState.readNbt(playersTag.getCompound(key));

      UUID uuid = UUID.fromString(key);
      serverState.players.put(uuid, playerState);
    });

    return serverState;
  }

  /**
   * Get the player's hotbar inventory if we're currently running on the server, and the player has a modded client.
   *
   * Otherwise, `null` is returned.
   * @param player The player to examine
   * @return {@code null} if not running on the server, otherwise the player's additional hotbars.
   */
  @Nullable
  public static HotbarInventory getPlayerState(PlayerEntity player) {
    if (player.world.getServer() != null) {
      ServerState serverState = getServerState(player.world.getServer());

      var partial = serverState.players.computeIfAbsent(player.getUuid(), uuid -> new HotbarInvState());

      return new HotbarInventory(partial, (ServerPlayerEntity) player);
    } else {
      return null;
    }
  }
}
