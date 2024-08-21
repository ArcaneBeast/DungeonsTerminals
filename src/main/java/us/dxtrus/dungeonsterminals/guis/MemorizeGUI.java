package us.dxtrus.dungeonsterminals.guis;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import us.dxtrus.commons.utils.StringUtils;
import us.dxtrus.dungeonsterminals.DungeonsTerminals;
import us.dxtrus.dungeonsterminals.api.TerminalCompleteEvent;
import us.dxtrus.dungeonsterminals.models.Terminal;
import us.dxtrus.dungeonsterminals.models.TerminalType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemorizeGUI extends TerminalGUI {
    private static final int PREVIEW_SECONDS = 3;

    private final List<Material> correctItems = new ArrayList<>();
    private final Map<Material, Boolean> guessed = new HashMap<>();

    private final BukkitTask startGuessing;

    public MemorizeGUI(Player player, Terminal terminal, JavaPlugin plugin) {
        super(terminal, player);

        chooseRandomItems();
        showRandomItems();
        startGuessing = Bukkit.getScheduler().runTaskLater(plugin, this::startGuessing, 20L * PREVIEW_SECONDS);
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        if (startGuessing != null) {
            startGuessing.cancel();
        }
    }

    private void chooseRandomItems() {
        for (int i = 0; i < 3; i++) {
            Material[] materials = Material.values();
            Material material = materials[DungeonsTerminals.getRandom().nextInt(materials.length)];
            if (correctItems.contains(material) || material.isAir() || material == Material.ALLAY_SPAWN_EGG || !material.isItem()) {
                --i;
                continue;
            }
            correctItems.add(material);
        }
    }

    private Material randomMaterialNotInCorrect() {
        for (int i = 0; i < 2; i++) {
            Material[] materials = Material.values();
            Material material = materials[DungeonsTerminals.getRandom().nextInt(materials.length)];
            if (correctItems.contains(material) || material.isAir() || !material.isItem()) {
                --i;
                continue;
            }
            return material;
        }
        return Material.ALLAY_SPAWN_EGG;
    }

    private void showRandomItems() {
        setItem(11, new ItemStack(correctItems.get(0)));
        setItem(13, new ItemStack(correctItems.get(1)));
        setItem(15, new ItemStack(correctItems.get(2)));
    }

    private void startGuessing() {
        clearPreview();
        showGuessOptions();
    }

    private void clearPreview() {
        setItem(11, new ItemStack(Material.AIR));
        setItem(13, new ItemStack(Material.AIR));
        setItem(15, new ItemStack(Material.AIR));
    }

    private void showGuessOptions() {
        int firstSlot = DungeonsTerminals.getRandom().nextInt(TerminalType.MEMORIZE.getGuiSize()-1);
        int secondSlot = DungeonsTerminals.getRandom().nextInt(TerminalType.MEMORIZE.getGuiSize()-1);
        int thirdSlot = DungeonsTerminals.getRandom().nextInt(TerminalType.MEMORIZE.getGuiSize()-1);

        Material matOne = correctItems.remove(0);
        setItem(firstSlot, new ItemStack(matOne), e -> {
            guessed.put(matOne, true);
            removeItem(e.getSlot());
            if (guessed.size() == 3) {
                completeTerminal();
            }
        });

        Material matTwo = correctItems.remove(0);
        setItem(secondSlot, new ItemStack(matTwo), e -> {
            guessed.put(matTwo, true);
            removeItem(e.getSlot());
            if (guessed.size() == 3) {
                completeTerminal();
            }
        });

        Material matThree = correctItems.remove(0);
        setItem(thirdSlot, new ItemStack(matThree), e -> {
            guessed.put(matThree, true);
            removeItem(e.getSlot());
            if (guessed.size() == 3) {
                completeTerminal();
            }
        });

        for (int i = 0; i < TerminalType.MEMORIZE.getGuiSize()-4; i++) {
            addItem(new ItemStack(randomMaterialNotInCorrect()));
        }
    }

    @Override
    protected void completeTerminal() {
        getInventory().close();
        player.sendMessage(StringUtils.modernMessage("&aTerminal Complete!"));
        new TerminalCompleteEvent(player, terminal).callEvent();
    }
}
