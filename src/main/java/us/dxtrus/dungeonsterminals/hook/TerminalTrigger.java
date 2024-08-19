package us.dxtrus.dungeonsterminals.hook;

import net.playavalon.mythicdungeons.MythicDungeons;
import net.playavalon.mythicdungeons.api.annotations.DeclaredTrigger;
import net.playavalon.mythicdungeons.api.annotations.SavedField;
import net.playavalon.mythicdungeons.api.parents.DungeonTrigger;
import net.playavalon.mythicdungeons.api.parents.TriggerCategory;
import net.playavalon.mythicdungeons.menu.MenuButton;
import net.playavalon.mythicdungeons.menu.menuitems.ChatMenuItem;
import net.playavalon.mythicdungeons.utility.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import us.dxtrus.dungeonsterminals.api.TerminalCompleteEvent;

@DeclaredTrigger
public final class TerminalTrigger extends DungeonTrigger {
    @SavedField
    private String terminalId;

    public TerminalTrigger() {
        super("Terminal Complete");
        this.waitForConditions = true;
        this.category = TriggerCategory.DUNGEON;
    }

    public void init() {
        super.init();
        this.setDisplayName(this.terminalId + " Terminal Complete");
    }

    @EventHandler
    public void onTerminalComplete(TerminalCompleteEvent event) {
        if (!event.getTerminal().getId().equals(this.terminalId)) return;
        if (!event.getTerminal().getAssociatedDungeon().equals(this.getInstance().getDungeon().getFolder().getName())) return;
        this.trigger(MythicDungeons.inst().getMythicPlayer(event.getPlayer()));
    }

    @Override
    public MenuButton buildMenuButton() {
        MenuButton functionButton = new MenuButton(Material.COMPARATOR);
        functionButton.setDisplayName("&dTerminal Link");
        functionButton.addLore("&eTriggered when a certain terminal");
        functionButton.addLore("&eis successfully complete.");
        return functionButton;
    }

    @Override
    public void buildHotbarMenu() {
        this.menu.addMenuItem(new ChatMenuItem() {
            public void buildButton() {
                this.button = new MenuButton(Material.PAPER);
                this.button.setDisplayName("&d&lTerminal ID");
            }

            public void onSelect(Player player) {
                player.sendMessage(Util.colorize(MythicDungeons.debugPrefix + "&eWhat terminal should this trigger link to? ('any' for any mob.)"));
                player.sendMessage(Util.colorize(MythicDungeons.debugPrefix + "&eTerminal Id is currently: &6" + TerminalTrigger.this.terminalId));
            }

            public void onInput(Player player, String message) {
                TerminalTrigger.this.terminalId = message;
                player.sendMessage(Util.colorize(MythicDungeons.debugPrefix + "&aSet linked terminal id to '&6" + message + "&a'"));
                TerminalTrigger.this.setDisplayName(TerminalTrigger.this.terminalId + " Terminal Complete");
            }
        });
    }
}
