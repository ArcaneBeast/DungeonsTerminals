package us.dxtrus.dungeonsterminals.data.persistent;

import net.playavalon.mythicdungeons.MythicDungeons;
import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.configuration.file.YamlConfiguration;
import us.dxtrus.commons.database.DatabaseHandler;
import us.dxtrus.commons.database.DatabaseObject;
import us.dxtrus.commons.database.dao.Dao;
import us.dxtrus.dungeonsterminals.data.persistent.daos.TerminalDao;
import us.dxtrus.dungeonsterminals.models.Terminal;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class YamlHandler implements DatabaseHandler {
    private final Map<Class<?>, Dao<? extends DatabaseObject>> daos = new HashMap<>();
    private boolean connected = false;
    private YamlConfiguration configuration;
    private File file;

    public YamlHandler() {
        load();
        this.configuration = YamlConfiguration.loadConfiguration(this.file);

        registerDaos();
        connected = true;
    }

    @Override
    public <T extends DatabaseObject> List<T> getAll(Class<T> aClass) {
        return (List<T>) getDao(aClass).getAll();
    }

    @Override
    public <T extends DatabaseObject> Optional<T> get(Class<T> aClass, UUID uuid) {
        return (Optional<T>) getDao(aClass).get(uuid);
    }

    @Override
    public <T extends DatabaseObject> Optional<T> search(Class<T> aClass, String s) {
        return (Optional<T>) getDao(aClass).get(s);
    }

    @Override
    public <T extends DatabaseObject> void save(Class<T> aClass, T t) {
        getDao(aClass).save(t);
    }

    @Override
    public <T extends DatabaseObject> void update(Class<T> aClass, T t, String[] strings) {
        getDao(aClass).update(t, strings);
    }

    @Override
    public <T extends DatabaseObject> void delete(Class<T> aClass, T t) {
        getDao(aClass).delete(t);
    }

    @Override
    public <T extends DatabaseObject> void deleteSpecific(Class<T> aClass, T t, Object o) {
        getDao(aClass).deleteSpecific(t, o);
    }

    @Override
    public void registerDao(Class<?> aClass, Dao<? extends DatabaseObject> dao) {
        daos.put(aClass, dao);
    }

    public void registerDaos() {
        registerDao(Terminal.class, new TerminalDao(configuration, file));
    }

    /**
     * Gets the DAO for a specific class.
     *
     * @param clazz The class to get the DAO for.
     * @param <T>   The type of the class.
     * @return The DAO for the specified class.
     */
    private <T extends DatabaseObject> Dao<T> getDao(Class<?> clazz) {
        if (!daos.containsKey(clazz))
            throw new IllegalArgumentException("No DAO registered for class " + clazz.getName());
        return (Dao<T>) daos.get(clazz);
    }

    public void load() {
        try {
            this.file = new File(MythicDungeons.inst().getDataFolder(), "terminals.yml");
            if (!this.file.exists()) {
                this.file.createNewFile();
            }
            this.configuration = YamlConfiguration.loadConfiguration(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void connect() {
        // do nothing
    }

    @Override
    public void destroy() {
        // do nothing
    }

    @Override
    public void wipeDatabase() {
        throw new NotImplementedException();
    }
}
