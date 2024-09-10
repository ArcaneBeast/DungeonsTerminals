package us.dxtrus.dungeonsterminals.commands;

import net.playavalon.mythicdungeons.MythicDungeons;
import net.playavalon.mythicdungeons.player.MythicPlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import us.dxtrus.commons.command.BasicSubCommand;
import us.dxtrus.commons.command.Command;
import us.dxtrus.commons.command.user.BukkitUser;
import us.dxtrus.commons.command.user.CommandUser;
import us.dxtrus.commons.utils.StringUtils;
import us.dxtrus.dungeonsterminals.config.Config;
import us.dxtrus.dungeonsterminals.data.CacheManager;
import us.dxtrus.dungeonsterminals.data.DatabaseManager;
import us.dxtrus.dungeonsterminals.models.LocRef;
import us.dxtrus.dungeonsterminals.models.Terminal;

import java.util.List;
import java.util.Optional;

public class RemoveSubCommand extends BasicSubCommand {
    @Command(name = "remove", permission = "terminals.admin")
    public RemoveSubCommand() {
        super();
    }

    @Override
    public void execute(CommandUser commandUser, String[] strings) {
        Player player = ((BukkitUser) commandUser).getPlayer();
        if (strings.length >= 1) {
            handleWithId(MythicDungeons.inst().getMythicPlayer(player), strings[0]);
            return;
        }
        handleWithLocation(MythicDungeons.inst().getMythicPlayer(player));
    }

    private void handleWithLocation(MythicPlayer mythicPlayer) {
        Config.Commands conf = Config.getInstance().getCommands();
        if (mythicPlayer.getInstance() == null || !mythicPlayer.getInstance().isEditInstance()) {
            mythicPlayer.getPlayer().sendMessage(StringUtils.legacyMessage(conf.getMustBeEditing()));
            return;
        }

        String dungeon = mythicPlayer.getInstance().getDungeon().getFolder().getName();

        Block targetBlock = mythicPlayer.getPlayer().getTargetBlockExact(5);

        if (targetBlock == null) {
            mythicPlayer.getPlayer().sendMessage(StringUtils.legacyMessage(conf.getMustBeLookingAtBlock()));
            return;
        }

        LocRef loc = LocRef.fromLocation(targetBlock.getLocation());

        Optional<Terminal> possibleTerminal = CacheManager.getInstance().get(loc);

        if (possibleTerminal.isEmpty()) {
            mythicPlayer.getPlayer().sendMessage(StringUtils.legacyMessage(conf.getNoTerminalAtLoc()));
            return;
        }

        CacheManager.getInstance().invalidate(possibleTerminal.get());
        DatabaseManager.getInstance().delete(Terminal.class, possibleTerminal.get())
                .thenRun(() -> mythicPlayer.getPlayer().sendMessage(StringUtils.legacyToMiniMessage(conf.getTerminalRemoved()
                        .formatted(possibleTerminal.get().getType().getName(), loc.toString(), dungeon))));
    }

    private void handleWithId(MythicPlayer mythicPlayer, String terminalId) {
        Config.Commands conf = Config.getInstance().getCommands();
        if (mythicPlayer.getInstance() == null || !mythicPlayer.getInstance().isEditInstance()) {
            mythicPlayer.getPlayer().sendMessage(StringUtils.legacyMessage(conf.getMustBeEditing()));
            return;
        }

        String dungeon = mythicPlayer.getInstance().getDungeon().getFolder().getName();

        Optional<Terminal> possibleTerminal = CacheManager.getInstance().get(terminalId);

        if (possibleTerminal.isEmpty()) {
            mythicPlayer.getPlayer().sendMessage(StringUtils.legacyMessage(conf.getNoTerminalWithId()));
            return;
        }

        CacheManager.getInstance().invalidate(possibleTerminal.get());
        DatabaseManager.getInstance().delete(Terminal.class, possibleTerminal.get())
                .thenRun(() -> mythicPlayer.getPlayer().sendMessage(StringUtils.legacyToMiniMessage(conf.getTerminalRemoved()
                        .formatted(possibleTerminal.get().getType().getName(), possibleTerminal.get().getLocation().toString(), dungeon))));
    }

    @Override
    public List<String> tabComplete(CommandUser sender, String[] args) {
        if (args.length == 1) {
            return CacheManager.getInstance().getAllIds();
        }
        return List.of();
    }
}
