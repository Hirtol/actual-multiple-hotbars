package top.hirtol.actualmultiplehotbars;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import top.hirtol.actualmultiplehotbars.inventory.ServerInventoryManager;

public class CommandHandler {

  public static void init() {
    CommandRegistrationEvent.EVENT.register(((dispatcher, selection) -> dispatcher.register(
        literal("amh")
            .requires(src -> src.hasPermissionLevel(4))
            .then(literal("restore").then(argument("player", EntityArgumentType.players())
                .executes(ctx -> restoreHotbars(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "player")))))
    )));
  }

  private static int restoreHotbars(ServerCommandSource source, ServerPlayerEntity entity) {
    if (ServerInventoryManager.restoreHistoricInventory(entity)) {
      MutableText text = new LiteralText("Successfully restored most recent hotbar state for ").append(entity.getName());
      text.setStyle(Style.EMPTY.withColor(Formatting.GREEN));
      source.sendFeedback(text, false);
    } else {
      MutableText text = new LiteralText("Failed to restore hotbar for ").append(entity.getName());
      text.setStyle(Style.EMPTY.withColor(Formatting.RED));
      source.sendFeedback(text, false);
    }

    return 1;
  }

}
