package us.dxtrus.commons.command.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.dxtrus.dungeonsterminals.DungeonsTerminals;

import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BukkitUser implements CommandUser {
    private final Player player;
    private final UUID uniqueId;
    private final String name;

    public static BukkitUser wrap(Player player) {
        return new BukkitUser(player, player.getUniqueId(), player.getName());
    }

    @Override
    public @NotNull Audience getAudience() {
        return DungeonsTerminals.getInstance().getAudiences().player(player);
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return player.hasPermission(permission);
    }
}
