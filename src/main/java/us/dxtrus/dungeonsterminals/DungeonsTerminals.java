package us.dxtrus.dungeonsterminals;

import net.playavalon.mythicdungeons.MythicDungeons;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import us.dxtrus.commons.command.BukkitCommandManager;
import us.dxtrus.dungeonsterminals.commands.TerminalsCommand;
import us.dxtrus.dungeonsterminals.hook.TerminalTrigger;
import us.dxtrus.dungeonsterminals.listener.TerminalsListener;

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

        MythicDungeons.inst().registerTrigger(TerminalTrigger.class);
        Bukkit.getPluginManager().registerEvents(new TerminalsListener(this), this);
        BukkitCommandManager.getInstance().registerCommand(new TerminalsCommand(this));
    }

    @Override
    public void onDisable() {

    }

    public static Random getRandom() {
        return random;
    }
}
