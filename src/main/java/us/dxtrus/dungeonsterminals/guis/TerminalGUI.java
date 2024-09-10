package us.dxtrus.dungeonsterminals.guis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.dxtrus.commons.cooldowns.Cooldown;
import us.dxtrus.commons.gui.FastInv;
import us.dxtrus.commons.utils.StringUtils;
import us.dxtrus.dungeonsterminals.DungeonsTerminals;
import us.dxtrus.dungeonsterminals.api.TerminalCompleteEvent;
import us.dxtrus.dungeonsterminals.models.Terminal;

public abstract class TerminalGUI extends FastInv {
    protected final Player player;
    protected final Terminal terminal;

    public TerminalGUI(Terminal terminal, Player player) {
        super(terminal.getType().getGuiSize(), terminal.getType().getTitle());
        this.player = player;
        this.terminal = terminal;
    }

    protected void completeTerminal() {
        player.closeInventory();
        player.sendMessage(StringUtils.legacyMessage("&aTerminal Complete!"));
        TerminalCompleteEvent event = new TerminalCompleteEvent(player, terminal);
        Bukkit.getPluginManager().callEvent(event);
    }


    protected void failTerminal() {
        player.closeInventory();
        player.sendMessage(StringUtils.legacyToMiniMessage("&cTerminal Failed!"));
        Cooldown cd = Cooldown.local("terminal_fail", player.getUniqueId(), 6000L);
        DungeonsTerminals.failCooldowns.put(player.getUniqueId(), cd);
        cd.start();
    }
}
