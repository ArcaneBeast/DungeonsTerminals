package us.dxtrus.dungeonsterminals.listener;

import net.playavalon.mythicdungeons.MythicDungeons;
import net.playavalon.mythicdungeons.api.events.dungeon.RemoteTriggerEvent;
import net.playavalon.mythicdungeons.dungeons.triggers.TriggerRemote;
import net.playavalon.mythicdungeons.player.MythicPlayer;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import us.dxtrus.commons.cooldowns.Cooldown;
import us.dxtrus.commons.cooldowns.CooldownReponse;
import us.dxtrus.commons.utils.StringUtils;
import us.dxtrus.dungeonsterminals.DungeonsTerminals;
import us.dxtrus.dungeonsterminals.api.TerminalCompleteEvent;
import us.dxtrus.dungeonsterminals.config.Config;
import us.dxtrus.dungeonsterminals.data.CacheManager;
import us.dxtrus.dungeonsterminals.guis.MemorizeGUI;
import us.dxtrus.dungeonsterminals.guis.SwitchGUI;

public class TerminalsListener implements Listener {
    private final DungeonsTerminals plugin;

    public TerminalsListener(DungeonsTerminals plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockClick(PlayerInteractEvent e) {
        CooldownReponse cdR = Cooldown.localFromClass(TerminalsListener.class, e.getPlayer().getUniqueId(), 100L).execute();
        if (!cdR.shouldContinue()) return;

        Block block = e.getClickedBlock();
        if (block == null) return;
        MythicPlayer player = MythicDungeons.inst().getMythicPlayer(e.getPlayer());
        if (player.getInstance() == null || !player.getInstance().isPlayInstance() || !player.getInstance().asPlayInstance().isStarted()) return;
        CacheManager.getInstance().get(block.getLocation()).ifPresent(terminal -> {
            if (!player.getInstance().getDungeon().getFolder().getName().equals(terminal.getAssociatedDungeon())) return;
            Cooldown cd = DungeonsTerminals.failCooldowns.get(player.getPlayer().getUniqueId());
            if (cd != null && cd.isActive()) {
                player.getPlayer().sendMessage(StringUtils.legacyMessage(Config.getInstance().getCooldownMessage()
                        .formatted(cd.remainingTime() / 1000D)));
                return;
            }
            switch (terminal.getType()) {
                case MEMORIZE -> new MemorizeGUI(terminal, player.getPlayer(), plugin).open(player.getPlayer());
                case SWITCHES -> new SwitchGUI(terminal, player.getPlayer(), plugin).open(player.getPlayer());
                default -> throw new NotImplementedException();
            }
        });
    }

    @EventHandler
    public void onTerminalComplete(TerminalCompleteEvent event) {
        MythicPlayer mythicPlayer = MythicDungeons.inst().getMythicPlayer(event.getPlayer());

        if (mythicPlayer.getInstance() == null) return;
        if (mythicPlayer.getInstance().asPlayInstance() == null) return;
        if (!event.getTerminal().getAssociatedDungeon().equals(mythicPlayer.getInstance().getDungeon().getFolder().getName())) return;

        TriggerRemote remoteTrig = new TriggerRemote();
        remoteTrig.setTriggerName(event.getTerminal().getId());
        RemoteTriggerEvent e = new RemoteTriggerEvent(remoteTrig.getTriggerName(), remoteTrig, mythicPlayer.getInstance().asPlayInstance());
        Bukkit.getPluginManager().callEvent(e);
//        TerminalTrigger terminalTrigger = new TerminalTrigger();
//        terminalTrigger.setTerminalId(event.getTerminal().getId());
//        terminalTrigger.trigger(mythicPlayer);
    }
}
