package top.hirtol.actualmultiplehotbars.forge.integrations;

import java.util.List;
import java.util.Set;
import net.minecraftforge.fml.loading.FMLLoader;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class MixinPlugin implements IMixinConfigPlugin {

  private static final Logger logger = LoggerFactory.getLogger(MixinPlugin.class);

  @Override
  public void onLoad(String mixinPackage) {

  }

  @Override
  public String getRefMapperConfig() {
    return null;
  }

  @Override
  public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
    return mixinClassName.contains("TheVault") && isLoaded("the_vault");
  }

  /**
   * Separate method, needed as `ModList.get().isLoaded()` is not yet properly initialised.
   * @param modid The mod's ID
   */
  public static boolean isLoaded(String modid) {
    return FMLLoader.getLoadingModList().getModFileById(modid) != null;
  }

  @Override
  public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

  }

  @Override
  public List<String> getMixins() {
    return null;
  }

  @Override
  public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

  }

  @Override
  public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

  }
}
