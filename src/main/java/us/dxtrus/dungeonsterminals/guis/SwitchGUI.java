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
import us.dxtrus.dungeonsterminals.config.Config;
import us.dxtrus.dungeonsterminals.models.Terminal;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SwitchGUI extends TerminalGUI {
    private final BukkitTask timer;
    private final Config.Terminals.Switches conf;
    private boolean firstSwitch;
    private boolean secondSwitch;
    private boolean thirdSwitch;
    private boolean forthSwitch;
    private boolean fifthSwitch;

    public SwitchGUI(Terminal terminal, Player player, DungeonsTerminals plugin) {
        super(terminal, player);

        this.conf = Config.getInstance().getTerminals().getSwitches();

        firstSwitch = plugin.getRandomBoolean(conf.getChances().getFirst());
        secondSwitch = plugin.getRandomBoolean(conf.getChances().getSecond());
        thirdSwitch = plugin.getRandomBoolean(conf.getChances().getThird());
        forthSwitch = plugin.getRandomBoolean(conf.getChances().getForth());
        fifthSwitch = plugin.getRandomBoolean(conf.getChances().getFifth());

        displaySwitches();

        timer = Bukkit.getScheduler().runTaskLater(plugin, this::failTerminal, 20L * conf.getTimeAllowed());
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        timer.cancel();
    }

    private void displaySwitches() {
        setItem(11, firstSwitch ? onItem() : offItem(), firstSwitch ? onConsumer() : offConsumer());
        setItem(12, secondSwitch ? onItem() : offItem(), secondSwitch ? onConsumer() : offConsumer());
        setItem(13, thirdSwitch ? onItem() : offItem(), thirdSwitch ? onConsumer() : offConsumer());
        setItem(14, forthSwitch ? onItem() : offItem(), forthSwitch ? onConsumer() : offConsumer());
        setItem(15, fifthSwitch ? onItem() : offItem(), fifthSwitch ? onConsumer() : offConsumer());
    }

    private boolean isAllOn() {
        return firstSwitch && secondSwitch && thirdSwitch && forthSwitch && fifthSwitch;
    }

    private ItemStack onItem() {
        List<String> colorful = new ArrayList<>();
        for (String str : conf.getOnButton().getLore()) {
            colorful.add(Util.colorize(str));
        }
        return new ItemBuilder(Material.LIME_STAINED_GLASS_PANE)
                .name(Util.colorize(conf.getOnButton().getName()))
                .lore(colorful).build();
    }

    private ItemStack offItem() {
        List<String> colorful = new ArrayList<>();
        for (String str : conf.getOffButton().getLore()) {
            colorful.add(Util.colorize(str));
        }
        return new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
                .name(Util.colorize(conf.getOffButton().getName()))
                .lore(colorful).build();
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
                    forthSwitch = false;
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
                    forthSwitch = true;
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
