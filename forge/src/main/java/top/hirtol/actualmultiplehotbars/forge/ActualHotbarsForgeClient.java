package top.hirtol.actualmultiplehotbars.forge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hirtol.actualmultiplehotbars.ActualHotbarsClient;

public class ActualHotbarsForgeClient {

  private static final Logger logger = LoggerFactory.getLogger(ActualHotbarsForgeClient.class);

  public static void init() {
    ActualHotbarsClient.init();
  }

}
