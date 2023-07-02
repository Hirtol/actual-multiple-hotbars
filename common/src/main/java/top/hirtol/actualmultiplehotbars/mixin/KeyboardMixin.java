package top.hirtol.actualmultiplehotbars.mixin;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hirtol.actualmultiplehotbars.config.Config;
import top.hirtol.actualmultiplehotbars.client.MultiClientState;

@Environment(EnvType.CLIENT)
@Mixin(Keyboard.class)
public abstract class KeyboardMixin {

  private static final Logger logger = LoggerFactory.getLogger(KeyboardMixin.class);

  @Final
  @Shadow
  private MinecraftClient client;

  @Inject(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/InputUtil;fromKeyCode(II)Lnet/minecraft/client/util/InputUtil$Key;"), cancellable = true)
  private void renderHotbarFrame(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
    var config = Config.getInstance();
    // If control isn't pressed just ignore
    if (modifiers == 0 || action != GLFW.GLFW_PRESS) {
      return;
    }
    // Quick way to check if we have a number 1..=9
    int rowToEquip = (key - GLFW.GLFW_KEY_1) + 1;
    if (rowToEquip < 1 || rowToEquip > 9) {
      return;
    }

    if ((modifiers & config.getClientSettings().swapModifierKey.toGlfwModifier()) != 0) {
      ci.cancel();
      MultiClientState.getInstance().getProvider().swapRow(client.player, rowToEquip);
    } else if ((modifiers & config.getClientSettings().secondaryEquipKey.toGlfwModifier()) != 0) {
      if (config.getAdditionalHotbars() >= 1) {
        MultiClientState.getInstance().getProvider().swapRow(client.player, 1);
      }
    }
  }
}