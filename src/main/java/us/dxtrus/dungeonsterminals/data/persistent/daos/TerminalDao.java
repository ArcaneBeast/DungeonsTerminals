package us.dxtrus.dungeonsterminals.data.persistent.daos;

import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import us.dxtrus.commons.database.dao.Dao;
import us.dxtrus.dungeonsterminals.models.LocRef;
import us.dxtrus.dungeonsterminals.models.Terminal;
import us.dxtrus.dungeonsterminals.models.TerminalType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TerminalDao implements Dao<Terminal> {
    private final YamlConfiguration configuration;
    private final File file;

    public TerminalDao(YamlConfiguration configuration, File file) {
        this.configuration = configuration;
        this.file = file;
    }

    @Override
    public Optional<Terminal> get(UUID uuid) {
        throw new NotImplementedException();
    }

    @Override
    public Optional<Terminal> get(String id) {
        ConfigurationSection conf = configuration.getConfigurationSection(id);
        if (conf == null) {
            return Optional.empty();
        }
        TerminalType type = TerminalType.fromString(conf.getString("type", "INVALID"));
        if (type == null) {
            throw new RuntimeException("Terminal %s type does not exist.".formatted(id));
        }
        String dungeon = conf.getString("dungeon");
        if (dungeon == null) {
            throw new RuntimeException("Terminal %s dungeon is not set.".formatted(id));
        }
        LocRef loc = new LocRef(conf.getDouble("x"), conf.getDouble("y"), conf.getDouble("z"));
        return Optional.of(new Terminal(id, type, dungeon, loc));
    }

    @Override
    public List<Terminal> getAll() {
        List<Terminal> ret = new ArrayList<>();
        for (String id : configuration.getKeys(false)) {
            get(id).ifPresent(ret::add);
        }
        return ret;
    }

    @Override
    public void save(Terminal terminal) {
        ConfigurationSection conf = configuration.getConfigurationSection(terminal.getId());
        if (conf != null) {
            throw new RuntimeException("A terminal of this ID already exists!");
        }
        configuration.set(terminal.getId() + ".type", terminal.getType().name());
        configuration.set(terminal.getId() + ".dungeon", terminal.getAssociatedDungeon());
        configuration.set(terminal.getId() + ".x", terminal.getLocation().getX());
        configuration.set(terminal.getId() + ".y", terminal.getLocation().getY());
        configuration.set(terminal.getId() + ".z", terminal.getLocation().getZ());

        save();
    }

    @Override
    public void update(Terminal terminal, String[] strings) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(Terminal terminal) {
        ConfigurationSection conf = configuration.getConfigurationSection(terminal.getId());
        if (conf == null) {
            return;
        }
        configuration.set(terminal.getId(), null);
        save();
    }

    private void save() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
