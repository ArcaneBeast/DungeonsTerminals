package us.dxtrus.commons.command.user;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class ConsoleUser implements CommandUser {
    @NotNull private final Audience audience;

    @Override
    @NotNull
    public Audience getAudience() {
        return audience;
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return true;
    }
}