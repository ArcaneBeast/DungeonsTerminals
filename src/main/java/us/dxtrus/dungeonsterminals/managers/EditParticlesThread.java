package us.dxtrus.dungeonsterminals.managers;

import net.playavalon.mythicdungeons.MythicDungeons;
import net.playavalon.mythicdungeons.player.MythicPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import us.dxtrus.dungeonsterminals.data.CacheManager;
import us.dxtrus.dungeonsterminals.models.Terminal;

import java.util.Optional;

public final class EditParticlesThread extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            MythicPlayer mythicPlayer = MythicDungeons.inst().getMythicPlayer(player);
            if (mythicPlayer == null) continue;
            if (!mythicPlayer.isEditMode()) continue;
            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.GREEN, 1.0F);
            for (String terminalId : CacheManager.getInstance().getAllIds()) {
                Optional<Terminal> terminal = CacheManager.getInstance().get(terminalId);
                if (terminal.isEmpty()) continue;
                Location location = terminal.get().getLocation().toBukkit(mythicPlayer.getInstance().getInstanceWorld());
                if (!(location.distance(player.getLocation()) > 15.0)) {
                    player.spawnParticle(Particle.REDSTONE, location, 12, 0.25, 0.25, 0.25, dustOptions);
                    player.spawnParticle(Particle.END_ROD, location, 1, 0.25, 0.25, 0.25, 0.01);
                }
            }
        }
    }
}
