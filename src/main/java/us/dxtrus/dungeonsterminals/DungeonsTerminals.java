package us.dxtrus.dungeonsterminals;

import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import us.dxtrus.commons.command.BukkitCommandManager;
import us.dxtrus.commons.cooldowns.Cooldown;
import us.dxtrus.commons.gui.FastInvManager;
import us.dxtrus.dungeonsterminals.commands.TerminalsCommand;
import us.dxtrus.dungeonsterminals.data.CacheManager;
import us.dxtrus.dungeonsterminals.data.DatabaseManager;
import us.dxtrus.dungeonsterminals.hook.Metrics;
import us.dxtrus.dungeonsterminals.listener.TerminalsListener;
import us.dxtrus.dungeonsterminals.managers.ParticlesThread;
import us.dxtrus.dungeonsterminals.models.Terminal;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class DungeonsTerminals extends JavaPlugin {
    public static final Map<UUID, Cooldown> failCooldowns = new ConcurrentHashMap<>();
    @Getter private static DungeonsTerminals instance;
    @Getter private final Random random = new Random(System.currentTimeMillis());
    @Getter private BukkitAudiences audiences;
    private Metrics metrics;

    @Override
    public void onEnable() {
        instance = this;
        audiences = BukkitAudiences.create(this);
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

        new ParticlesThread().runTaskTimerAsynchronously(this, 0L, 10L);
        metrics = new Metrics(this, 23252);
        metrics.addCustomChart(new Metrics.SingleLineChart("active_terminals", () -> CacheManager.getInstance().getAllIds().size()));
    }

    @Override
    public void onDisable() {
        if (metrics != null) {
            metrics.shutdown();
        }
    }

    public boolean getRandomBoolean(double percentage) {
        double randomDouble = random.nextDouble();
        return randomDouble < (percentage / 100);
    }
}
