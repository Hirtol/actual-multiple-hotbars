package top.hirtol.actualmultiplehotbars.mixin;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hirtol.actualmultiplehotbars.client.AMHClientState;
import top.hirtol.actualmultiplehotbars.config.AMHConfig;
import top.hirtol.actualmultiplehotbars.inventory.PlayerHotbarState;

@Environment(EnvType.CLIENT)
@Mixin(Keyboard.class)
public abstract class KeyboardMixin {

  private static final Logger logger = LogManager.getLogger(KeyboardMixin.class);

  @Final
  @Shadow
  private MinecraftClient client;

  @Inject(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/InputUtil;fromKeyCode(II)Lnet/minecraft/client/util/InputUtil$Key;"), cancellable = true)
  private void renderHotbarFrame(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
    AMHConfig config = AMHConfig.getInstance();
    // If control isn't pressed just ignore
    if (modifiers == 0 || action != GLFW.GLFW_PRESS) {
      return;
    }
    // Quick way to check if we have a number 1..=9
    // This is explicitly done so that pressing `1` will map to `0` (aka, MAIN_HOTBAR)
    int rowToEquip = (key - GLFW.GLFW_KEY_1);
    if (rowToEquip < 0 || rowToEquip > 8) {
      return;
    }

    if (AMHConfig.getInstance().getClientSettings().debugMode) {
      if (rowToEquip == 7) {
        logger.warn("Resetting hotbar states");
        AMHClientState.getInstance().getProvider().reset();
      }

      if (rowToEquip == 8) {
        PlayerHotbarState state = AMHClientState.getInstance().getHotbarInventory().getVirtualState();
        for (int i = 0; i < 4; i++) {
          System.out.println("Visual: " + i + " - Virtual: " + state.visualVirtualMappings.getInt(i) + " - Physical: "
                             + state.virtualPhysicalMappings.getInt(state.visualVirtualMappings.getInt(i)));
        }
      }
    }

    // Actual business logic
    if ((modifiers & config.getClientSettings().swapModifierKey.toGlfwModifier()) != 0) {
      ci.cancel();
      AMHClientState.getInstance().getProvider().equipVirtualHotbar(client.player, rowToEquip);
    }
  }
}