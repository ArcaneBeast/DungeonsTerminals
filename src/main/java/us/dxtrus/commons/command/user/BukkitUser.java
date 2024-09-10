package us.dxtrus.commons.command.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BukkitUser implements CommandUser {
    private final Player audience;
    private final UUID uniqueId;
    private final String name;

    public static BukkitUser wrap(Player player) {
        return new BukkitUser(player, player.getUniqueId(), player.getName());
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return audience.hasPermission(permission);
    }
}
