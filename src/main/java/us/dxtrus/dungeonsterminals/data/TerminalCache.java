package us.dxtrus.dungeonsterminals.data;

import org.bukkit.Location;
import us.dxtrus.dungeonsterminals.models.LocRef;
import us.dxtrus.dungeonsterminals.models.Terminal;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class TerminalCache {
    private static TerminalCache instance;

    private final Map<LocRef, Terminal> terminalCache = new ConcurrentHashMap<>();

    public void cache(Terminal terminal) {
        terminalCache.put(terminal.getLocation(), terminal);
    }

    public Optional<Terminal> get(Location location) {
        return Optional.ofNullable(terminalCache.get(LocRef.fromLocation(location)));
    }

    public static TerminalCache getInstance() {
        return instance == null ? instance = new TerminalCache() : instance;
    }
}
