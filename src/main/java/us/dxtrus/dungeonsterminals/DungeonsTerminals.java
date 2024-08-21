package us.dxtrus.dungeonsterminals;

import net.playavalon.mythicdungeons.MythicDungeons;
import net.playavalon.mythicdungeons.utility.GUIHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import us.dxtrus.commons.command.BukkitCommandManager;
import us.dxtrus.dungeonsterminals.commands.TerminalsCommand;
import us.dxtrus.dungeonsterminals.data.CacheManager;
import us.dxtrus.dungeonsterminals.data.DatabaseManager;
import us.dxtrus.dungeonsterminals.hook.TerminalTrigger;
import us.dxtrus.dungeonsterminals.listener.TerminalsListener;
import us.dxtrus.dungeonsterminals.managers.EditParticlesThread;
import us.dxtrus.dungeonsterminals.models.Terminal;

import java.util.Random;

public final class DungeonsTerminals extends JavaPlugin {
    private static final Random random = new Random(System.currentTimeMillis());

    @Override
    public void onEnable() {
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

        Bukkit.getPluginManager().registerEvents(new TerminalsListener(this), this);
        BukkitCommandManager.getInstance().registerCommand(new TerminalsCommand(this));

        CacheManager.getInstance(); // init
        DatabaseManager.getInstance(); // init
        DatabaseManager.getInstance().getAll(Terminal.class).thenAccept(CacheManager.getInstance()::update);

        new EditParticlesThread().runTaskTimerAsynchronously(this, 0L, 10L);
    }

    @Override
    public void onDisable() {

    }

    public static Random getRandom() {
        return random;
    }
}
