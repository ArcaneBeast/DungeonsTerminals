package us.dxtrus.dungeonsterminals.api;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import us.dxtrus.dungeonsterminals.models.Terminal;

// dont lombokify
public final class TerminalCompleteEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    private final Terminal terminal;

    public TerminalCompleteEvent(@NotNull Player who, @NotNull Terminal terminal) {
        super(who);
        this.terminal = terminal;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public Terminal getTerminal() {
        return terminal;
    }
}
