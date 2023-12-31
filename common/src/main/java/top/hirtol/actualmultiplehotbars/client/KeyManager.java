package top.hirtol.actualmultiplehotbars.client;

import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil.Type;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.config.AMHConfig;
import top.hirtol.actualmultiplehotbars.networking.packets.OpenInvC2SPacket;

public class KeyManager {

  private static final Logger logger = LoggerFactory.getLogger(KeyManager.class);
  public static KeyBinding rotateKey = new KeyBinding("key.actualmultiplehotbars.rotate", Type.KEYSYM,
      GLFW.GLFW_KEY_X, "category.actualmultiplehotbars.keybinds");

  public static KeyBinding reverseRotateKey =
      new KeyBinding("key.actualmultiplehotbars.rotate_reverse", Type.KEYSYM,
          GLFW.GLFW_KEY_UNKNOWN, "category.actualmultiplehotbars.keybinds");
  public static KeyBinding openHotbarKey = new KeyBinding("key.actualmultiplehotbars.openui", Type.KEYSYM,
      GLFW.GLFW_KEY_V, "category.actualmultiplehotbars.keybinds");
  public static KeyBinding changeVisibleHotbars =
      new KeyBinding("key.actualmultiplehotbars.increase_hotbars", Type.KEYSYM,
          GLFW.GLFW_KEY_KP_MULTIPLY, "category.actualmultiplehotbars.keybinds");

  public static void initialise() {
    KeyMappingRegistry.register(rotateKey);
    KeyMappingRegistry.register(reverseRotateKey);
    KeyMappingRegistry.register(openHotbarKey);
    KeyMappingRegistry.register(changeVisibleHotbars);

    ClientTickEvent.CLIENT_POST.register(client -> {
      var currentInstance = AMHClientState.getInstance();
      var config = AMHConfig.getInstance();

      if (rotateKey.wasPressed()) {
        currentInstance.getProvider().rotate(client.player, false);
      }
      if (reverseRotateKey.wasPressed()) {
        currentInstance.getProvider().rotate(client.player, true);
      }

      if (changeVisibleHotbars.wasPressed()) {
        config.getClientSettings().additionalVisibleHotbars =
            ((config.getClientSettings().additionalVisibleHotbars + 1) % (
                config.getAdditionalHotbars() + 1));
      }

      if (openHotbarKey.wasPressed()) {
        new OpenInvC2SPacket().send();
      }
    });
  }

}
