package top.hirtol.actualmultiplehotbars.client;

import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil.Type;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.networking.packets.OpenInvC2SPacket;

public class KeyManager {

  private static final Logger logger = LoggerFactory.getLogger(KeyManager.class);
  public static KeyBinding rotateKey = new KeyBinding("key.actualmultiplehotbars.swap", Type.KEYSYM,
      GLFW.GLFW_KEY_X, "category.actualmultiplehotbars.keybinds");
  public static KeyBinding openHotbarKey = new KeyBinding("key.actualmultiplehotbars.openui", Type.KEYSYM,
      GLFW.GLFW_KEY_V, "category.actualmultiplehotbars.keybinds");

  public static void initialise() {
    KeyMappingRegistry.register(rotateKey);
    KeyMappingRegistry.register(openHotbarKey);

    ClientTickEvent.CLIENT_POST.register(client -> {
      var currentInstance = MultiClientState.getInstance();
      if (rotateKey.wasPressed()) {
        currentInstance.getProvider().rotate(client.player);
      }

      if (openHotbarKey.wasPressed()) {
        new OpenInvC2SPacket().send();
      }
    });
  }

}
