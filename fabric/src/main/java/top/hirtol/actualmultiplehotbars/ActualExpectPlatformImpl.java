package top.hirtol.actualmultiplehotbars;

import top.hirtol.actualmultiplehotbars.ActualExpectPlatform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class ActualExpectPlatformImpl {
    /**
     * This is our actual method to {@link ActualExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
