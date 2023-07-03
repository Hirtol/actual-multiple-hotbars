package top.hirtol.actualmultiplehotbars.client;

import me.shedaniel.architectury.event.events.client.ClientTickEvent;
import me.shedaniel.architectury.registry.KeyBindings;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil.Type;
import org.lwjgl.glfw.GLFW;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.hirtol.actualmultiplehotbars.networking.packets.OpenInvC2SPacket;

public class KeyManager {

  private static final Logger logger = LogManager.getLogger(KeyManager.class);
  public static KeyBinding rotateKey = new KeyBinding("key.actualmultiplehotbars.swap", Type.KEYSYM,
      GLFW.GLFW_KEY_X, "category.actualmultiplehotbars.keybinds");
  public static KeyBinding openHotbarKey = new KeyBinding("key.actualmultiplehotbars.openui", Type.KEYSYM,
      GLFW.GLFW_KEY_V, "category.actualmultiplehotbars.keybinds");

  public static void initialise() {
    KeyBindings.registerKeyBinding(rotateKey);
    KeyBindings.registerKeyBinding(openHotbarKey);

    ClientTickEvent.CLIENT_POST.register(client -> {
      MultiClientState currentInstance = MultiClientState.getInstance();
      if (rotateKey.wasPressed()) {
        currentInstance.getProvider().rotate(client.player);
      }

      if (openHotbarKey.wasPressed()) {
        new OpenInvC2SPacket().send();
      }
    });
  }

}
