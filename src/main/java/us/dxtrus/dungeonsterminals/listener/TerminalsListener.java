package us.dxtrus.dungeonsterminals.listener;

import net.playavalon.mythicdungeons.MythicDungeons;
import net.playavalon.mythicdungeons.player.MythicPlayer;
import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import us.dxtrus.dungeonsterminals.data.CacheManager;
import us.dxtrus.dungeonsterminals.guis.MemorizeGUI;

public class TerminalsListener implements Listener {
    private final JavaPlugin plugin;

    public TerminalsListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockClick(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if (block == null) return;
        MythicPlayer player = MythicDungeons.inst().getMythicPlayer(e.getPlayer());
        if (!player.getInstance().isStarted()) return;
        CacheManager.getInstance().get(block.getLocation()).ifPresent(terminal -> {
            if (!player.getInstance().getDungeon().getFolder().getName().equals(terminal.getAssociatedDungeon())) return;
            switch (terminal.getType()) {
                case MEMORIZE -> new MemorizeGUI(player.getPlayer(), terminal, plugin).open(player.getPlayer());
                default -> throw new NotImplementedException();
            }
        });
    }
}
