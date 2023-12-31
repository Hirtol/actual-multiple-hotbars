package top.hirtol.actualmultiplehotbars;

import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.config.AMHConfig;
import top.hirtol.actualmultiplehotbars.inventory.ServerHotbarInventory;
import top.hirtol.actualmultiplehotbars.inventory.ServerInventoryManager;
import top.hirtol.actualmultiplehotbars.networking.PacketRegistry;
import top.hirtol.actualmultiplehotbars.networking.packets.HotbarInvS2CPacket;
import top.hirtol.actualmultiplehotbars.networking.packets.SyncS2CConfigPacket;
import top.hirtol.actualmultiplehotbars.screenhandlers.ScreenHandlers;

public class ActualHotbars {

  public static final String MOD_ID = "actualmultiplehotbars";
  public static final Logger logger = LoggerFactory.getLogger(ActualHotbars.class);

  public static void init() {
    AMHConfig.init();
    PacketRegistry.initialiseServer();
    ScreenHandlers.init();

    PlayerEvent.PLAYER_JOIN.register(player -> {
      // Keep config in-sync. WARNING -> This currently leaks memory, has to be a better way of doing this
      AMHConfig.onChange((configHolder, ConfigData) -> {
        var packet = new SyncS2CConfigPacket(ConfigData.serverSettings);
        packet.send(player);
        return ActionResult.PASS;
      });

      // Sync initial state.
      ServerHotbarInventory playerState = ServerInventoryManager.getPlayerState(player);
      var serverSettings = AMHConfig.getInstance().getServerSettings();
      var syncPacket = new SyncS2CConfigPacket(serverSettings);
      var packet = new HotbarInvS2CPacket(playerState);

      syncPacket.send(player);
      packet.send(player);
    });

    logger.info("Actual Multiple Hotbars has been initialised");
  }

  public static Identifier ID(String id) {
    return new Identifier(ActualHotbars.MOD_ID, id);
  }
}
