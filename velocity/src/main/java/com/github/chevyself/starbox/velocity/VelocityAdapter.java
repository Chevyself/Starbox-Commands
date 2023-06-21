package com.github.chevyself.starbox.velocity;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.CommandManagerBuilder;
import com.github.chevyself.starbox.adapters.Adapter;
import com.github.chevyself.starbox.common.DecoratedMessagesProvider;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.parsers.CommandMetadataParser;
import com.github.chevyself.starbox.parsers.CommandParser;
import com.github.chevyself.starbox.parsers.EmptyCommandMetadataParser;
import com.github.chevyself.starbox.registry.MiddlewareRegistry;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import com.github.chevyself.starbox.velocity.commands.VelocityCommand;
import com.github.chevyself.starbox.velocity.context.CommandContext;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.NonNull;

public class VelocityAdapter implements Adapter<CommandContext, VelocityCommand> {

  @NonNull private final ProxyServer proxyServer;

  public VelocityAdapter(@NonNull ProxyServer proxyServer) {
    this.proxyServer = proxyServer;
  }

  @Override
  public void onRegister(@NonNull VelocityCommand command) {
    proxyServer
        .getCommandManager()
        .register(
            command.getName(), command.getExecutor(), command.getAliases().toArray(new String[0]));
  }

  @Override
  public void onUnregister(@NonNull VelocityCommand command) {}

  @Override
  public void close() {}

  @Override
  public void registerDefaultProviders(
      @NonNull CommandManagerBuilder<CommandContext, VelocityCommand> builder,
      @NonNull ProvidersRegistry<CommandContext> registry) {}

  @Override
  public void registerDefaultMiddlewares(
      @NonNull CommandManagerBuilder<CommandContext, VelocityCommand> builder,
      @NonNull MiddlewareRegistry<CommandContext> middlewares) {}

  @Override
  public void onBuilt(@NonNull CommandManager<CommandContext, VelocityCommand> built) {}

  @Override
  public @NonNull CommandParser<CommandContext, VelocityCommand> createParser(
      @NonNull CommandManager<CommandContext, VelocityCommand> commandManager) {
    return new VelocityCommandParser(this, commandManager);
  }

  @Override
  public @NonNull CommandMetadataParser getDefaultCommandMetadataParser() {
    return new EmptyCommandMetadataParser();
  }

  @Override
  public @NonNull MessagesProvider<CommandContext> getDefaultMessaesProvider() {
    return new DecoratedMessagesProvider<>();
  }
}
