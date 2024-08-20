package us.dxtrus.dungeonsterminals.data;

import org.bukkit.Location;
import us.dxtrus.dungeonsterminals.models.LocRef;
import us.dxtrus.dungeonsterminals.models.Terminal;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class CacheManager {
    private static CacheManager instance;

    private final Map<LocRef, Terminal> terminalCache = new ConcurrentHashMap<>();

    public void cache(Terminal terminal) {
        terminalCache.put(terminal.getLocation(), terminal);
    }

    public Optional<Terminal> get(Location location) {
        return Optional.ofNullable(terminalCache.get(LocRef.fromLocation(location)));
    }

    public List<String> getAllIds() {
        return terminalCache.values().stream().map(Terminal::getId).toList();
    }

    public void update(List<Terminal> terminals) {
        terminals.forEach(this::cache);
    }

    public static CacheManager getInstance() {
        return instance == null ? instance = new CacheManager() : instance;
    }
}
