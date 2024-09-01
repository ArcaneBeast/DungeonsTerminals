package us.dxtrus.dungeonsterminals.guis;

import net.playavalon.mythicdungeons.utility.helpers.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import us.dxtrus.commons.gui.ItemBuilder;
import us.dxtrus.dungeonsterminals.DungeonsTerminals;
import us.dxtrus.dungeonsterminals.models.Terminal;

import java.util.function.Consumer;

public class SwitchGUI extends TerminalGUI {
    private boolean firstSwitch;
    private boolean secondSwitch;
    private boolean thirdSwitch;
    private boolean fourthSwitch;
    private boolean fifthSwitch;

    private final BukkitTask timer;

    public SwitchGUI(Terminal terminal, Player player, DungeonsTerminals plugin) {
        super(terminal, player);

        firstSwitch = plugin.getRandomBoolean(20);
        secondSwitch = plugin.getRandomBoolean(35);
        thirdSwitch = plugin.getRandomBoolean(50);
        fourthSwitch = plugin.getRandomBoolean(15);
        fifthSwitch = plugin.getRandomBoolean(40);

        timer = Bukkit.getScheduler().runTaskLater(plugin, this::failTerminal, 60L);

        displaySwitches();
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        timer.cancel();
    }

    private void displaySwitches() {
        setItem(11, firstSwitch ? onItem() : offItem(), firstSwitch ? onConsumer() : offConsumer());
        setItem(12, secondSwitch ? onItem() : offItem(), secondSwitch ? onConsumer() : offConsumer());
        setItem(13, thirdSwitch ? onItem() : offItem(), thirdSwitch ? onConsumer() : offConsumer());
        setItem(14, fourthSwitch ? onItem() : offItem(), fourthSwitch ? onConsumer() : offConsumer());
        setItem(15, fifthSwitch ? onItem() : offItem(), fifthSwitch ? onConsumer() : offConsumer());
    }

    private boolean isAllOn() {
        return firstSwitch && secondSwitch && thirdSwitch && fourthSwitch && fifthSwitch;
    }

    private ItemStack onItem() {
        return new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).name(Util.colorize("&a&lON")).lore(Util.colorize("&7Click to turn off")).build();
    }

    private ItemStack offItem() {
        return new ItemBuilder(Material.RED_STAINED_GLASS_PANE).name(Util.colorize("&c&lOFF")).lore(Util.colorize("&7Click to turn on")).build();
    }

    private Consumer<InventoryClickEvent> onConsumer() {
        return event -> {
            switch (event.getRawSlot()) {
                case 11: {
                    firstSwitch = false;
                    break;
                }
                case 12: {
                    secondSwitch = false;
                    break;
                }
                case 13: {
                    thirdSwitch = false;
                    break;
                }
                case 14: {
                    fourthSwitch = false;
                    break;
                }
                case 15: {
                    fifthSwitch = false;
                    break;
                }
            }
            setItem(event.getRawSlot(), offItem(), offConsumer());
        };
    }

    private Consumer<InventoryClickEvent> offConsumer() {
        return event -> {
            switch (event.getRawSlot()) {
                case 11: {
                    firstSwitch = true;
                    break;
                }
                case 12: {
                    secondSwitch = true;
                    break;
                }
                case 13: {
                    thirdSwitch = true;
                    break;
                }
                case 14: {
                    fourthSwitch = true;
                    break;
                }
                case 15: {
                    fifthSwitch = true;
                    break;
                }
            }
            setItem(event.getRawSlot(), onItem(), onConsumer());
            if (!isAllOn()) return;
            timer.cancel();
            completeTerminal();
        };
    }
}
