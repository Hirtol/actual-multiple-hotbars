package top.hirtol.actualmultiplehotbars.client;

import me.shedaniel.architectury.event.events.client.ClientTickEvent;
import me.shedaniel.architectury.registry.KeyBindings;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil.Type;
import org.lwjgl.glfw.GLFW;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.hirtol.actualmultiplehotbars.config.AMHConfig;
import top.hirtol.actualmultiplehotbars.networking.packets.OpenInvC2SPacket;

public class KeyManager {

  private static final Logger logger = LogManager.getLogger(KeyManager.class);
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
    KeyBindings.registerKeyBinding(rotateKey);
    KeyBindings.registerKeyBinding(reverseRotateKey);
    KeyBindings.registerKeyBinding(openHotbarKey);
    KeyBindings.registerKeyBinding(changeVisibleHotbars);

    ClientTickEvent.CLIENT_POST.register(client -> {
      AMHClientState currentInstance = AMHClientState.getInstance();
      AMHConfig config = AMHConfig.getInstance();

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
