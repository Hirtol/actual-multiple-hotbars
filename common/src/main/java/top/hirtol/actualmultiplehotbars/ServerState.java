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
import top.hirtol.actualmultiplehotbars.inventory.PartialHotbarInventory;

public class ServerState extends PersistentState {

  public HashMap<UUID, PartialHotbarInventory> players = new HashMap<>();
  private static final HashMap<UUID, Boolean> MODDED_PLAYERS = new HashMap<>();

  public ServerState() {
    super("players");
  }

  @Override
  public void fromTag(NbtCompound tag) {
    NbtCompound playersTag = tag.getCompound("players");
    playersTag.getKeys().forEach(key -> {
      PartialHotbarInventory playerState = PartialHotbarInventory.fromNbt(playersTag.getCompound(key));

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

  public static ServerState getServerState(MinecraftServer server) {
    PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

    return persistentStateManager.getOrCreate(
        ServerState::new,
        ActualHotbars.MOD_ID);
  }

  public static ServerState createFromNbt(NbtCompound tag) {
    ServerState serverState = new ServerState();

    serverState.fromTag(tag);

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
    if (player.world.getServer() != null && isPlayerModded((ServerPlayerEntity) player)) {
      ServerState serverState = getServerState(player.world.getServer());

      var partial = serverState.players.computeIfAbsent(player.getUuid(), uuid -> new PartialHotbarInventory());

      return new HotbarInventory(partial, (ServerPlayerEntity) player);
    } else {
      return null;
    }
  }

  public static boolean isPlayerModded(ServerPlayerEntity player) {
    return MODDED_PLAYERS.getOrDefault(player.getUuid(), true);
  }

  public static void setPlayerModded(ServerPlayerEntity player, boolean isModded) {
    MODDED_PLAYERS.put(player.getUuid(), isModded);
  }
}
