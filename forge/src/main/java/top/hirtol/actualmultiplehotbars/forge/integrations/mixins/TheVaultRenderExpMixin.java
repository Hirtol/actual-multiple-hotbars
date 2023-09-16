package top.hirtol.actualmultiplehotbars.forge.integrations.mixins;


import iskallia.vault.client.gui.overlay.VaultBarOverlay;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import top.hirtol.actualmultiplehotbars.config.AMHConfig;

@OnlyIn(Dist.CLIENT)
@Mixin(VaultBarOverlay.class)
public abstract class TheVaultRenderExpMixin {

  private static final Logger logger = LoggerFactory.getLogger(TheVaultRenderExpMixin.class);

  @ModifyVariable(method = "render", at = @At("HEAD"), ordinal = 1, argsOnly = true)
  public int modifyHeight(int height) {
    var settings = AMHConfig.getInstance().getClientSettings();
    return height - (settings.shift * settings.additionalVisibleHotbars);
  }
}
