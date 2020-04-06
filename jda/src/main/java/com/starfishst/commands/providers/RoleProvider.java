package com.starfishst.commands.providers;

import com.starfishst.commands.context.GuildCommandContext;
import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.providers.type.IArgumentProvider;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;

public class RoleProvider implements IArgumentProvider<Role> {

    @NotNull
    @Override
    public Role fromString(@NotNull String string, @NotNull ICommandContext<?> context)
            throws ArgumentProviderException {
        if (context instanceof GuildCommandContext) {
            for (Role role : ((GuildCommandContext) context).getMessage().getMentionedRoles()) {
                if (role.getAsMention().equalsIgnoreCase(string)) {
                    return role;
                }
            }
        } else {
            throw new ArgumentProviderException("This command may only be executed in a guild");
        }
        throw new ArgumentProviderException("{0} is not a valid role", string);
    }

    @Override
    public @NotNull Class<Role> getClazz() {
        return Role.class;
    }
}
