package com.github.chevyself.starbox.bukkit.result;

import com.github.chevyself.starbox.bukkit.StarboxBukkitCommand;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.bukkit.utils.Components;
import com.github.chevyself.starbox.result.StarboxResult;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

/**
 * This is the implementation for {@link StarboxResult} to be used in the execution of {@link
 * StarboxBukkitCommand}. This includes a {@link List} of {@link BaseComponent} which will be sent
 * to the {@link org.bukkit.command.CommandSender} upon the command execution.
 *
 * <p>If the {@link BukkitResult} from {@link StarboxBukkitCommand#execute(CommandContext)} is null
 * or empty no message will be shown to the sender.
 *
 * <p>Exceptions will show a simple {@link BukkitResult} message and the stack trace will be
 * printed.
 *
 * <p>To parse {@link BaseComponent} from a String use {@link Components#getComponent(String)} to
 * know how to create a {@link BaseComponent} check <a
 * href="https://minecraft.tools/en/tellraw.php">minecraft-tools</a> or the <a
 * href="https://ci.md-5.net/job/BungeeCord/ws/chat/target/apidocs/overview-summary.html">bungee-api-chat</a>.
 */
public interface BukkitResult extends StarboxResult {

  /**
   * Get the components to be sent to the {@link org.bukkit.command.CommandSender}.
   *
   * @return the list of components
   */
  @NonNull
  List<BaseComponent> getComponents();

  @Override
  default @NonNull Optional<String> getMessage() {
    return Optional.ofNullable(ComponentSerializer.toString(this.getComponents()));
  }
}
