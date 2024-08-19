package us.dxtrus.dungeonsterminals.guis;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import us.dxtrus.commons.utils.StringUtils;
import us.dxtrus.commons.utils.TaskManager;
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

    private final Player player;
    private final Terminal terminal;

    public MemorizeGUI(Player player, Terminal terminal, JavaPlugin plugin) {
        super(TerminalType.MEMORIZE);
        this.player = player;
        this.terminal = terminal;

        chooseRandomItems();
        showRandomItems();
        TaskManager.runSyncDelayed(plugin, this::startGuessing, 20L * PREVIEW_SECONDS);
    }

    private void chooseRandomItems() {
        for (int i = 0; i < 3; i++) {
            Material[] materials = Material.values();
            Material material = materials[DungeonsTerminals.getRandom().nextInt(materials.length)];
            if (correctItems.contains(material)) {
                i--;
                continue;
            }
            correctItems.add(material);
        }
    }

    private Material randomMaterialNotInCorrect() {
        Material[] materials = Material.values();
        for (Material material : materials) {
            if (correctItems.contains(material)) continue;
            return material;
        }
        return Material.ACACIA_BOAT;
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
        for (int i = 0; i < TerminalType.MEMORIZE.getGuiSize()-1; i++) {
            if (DungeonsTerminals.getRandom().nextBoolean()) {
                Material mat = correctItems.remove(0);
                setItem(i, new ItemStack(mat), e -> {
                    guessed.put(mat, true);
                    removeItem(e.getSlot());
                    if (guessed.size() == 3) {
                        completeTerminal();
                    }
                });
                continue;
            }
            setItem(i, new ItemStack(randomMaterialNotInCorrect()));
        }
    }

    @Override
    protected void completeTerminal() {
        player.sendMessage(StringUtils.modernMessage("&aTerminal Complete!"));
        new TerminalCompleteEvent(player, terminal).callEvent();
    }
}
