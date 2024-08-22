package us.dxtrus.dungeonsterminals;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import us.dxtrus.commons.command.BukkitCommandManager;
import us.dxtrus.commons.gui.FastInvManager;
import us.dxtrus.commons.holograms.HologramManager;
import us.dxtrus.dungeonsterminals.commands.TerminalsCommand;
import us.dxtrus.dungeonsterminals.data.CacheManager;
import us.dxtrus.dungeonsterminals.data.DatabaseManager;
import us.dxtrus.dungeonsterminals.listener.TerminalsListener;
import us.dxtrus.dungeonsterminals.managers.EditParticlesThread;
import us.dxtrus.dungeonsterminals.models.Terminal;

import java.util.Random;

public final class DungeonsTerminals extends JavaPlugin {
    private final Random random = new Random(System.currentTimeMillis());
    private static DungeonsTerminals instance;
    private final HologramManager hologramManager = new HologramManager(this, "");

    @Override
    public void onEnable() {
        instance = this;
        try {
            Class.forName("net.playavalon.mythicdungeons.MythicDungeons");
        } catch (ClassNotFoundException e) {
            getLogger().severe("Mythic Dungeons not installed! Disabling expansion...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        // disabled until mythic dungeons fixes the jankness of the api and this is actually possible.
//        MythicDungeons.inst().registerTrigger(TerminalTrigger.class);
//        GUIHandler.initTriggerMenu();

        FastInvManager.register(this);
        Bukkit.getPluginManager().registerEvents(new TerminalsListener(this), this);
        BukkitCommandManager.getInstance().registerCommand(new TerminalsCommand(this));

        CacheManager.getInstance(); // init
        DatabaseManager.getInstance(); // init
        DatabaseManager.getInstance().getAll(Terminal.class).thenAccept(CacheManager.getInstance()::update);

        new EditParticlesThread().runTaskTimerAsynchronously(this, 0L, 10L);
    }

    public HologramManager getHologramManager() {
        return hologramManager;
    }

    public Random getRandom() {
        return random;
    }

    public static DungeonsTerminals getInstance() {
        return instance;
    }
}
