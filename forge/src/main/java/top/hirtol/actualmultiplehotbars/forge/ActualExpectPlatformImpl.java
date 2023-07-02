package top.hirtol.actualmultiplehotbars.forge;

import top.hirtol.actualmultiplehotbars.ActualExpectPlatform;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ActualExpectPlatformImpl {
    /**
     * This is our actual method to {@link ActualExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
