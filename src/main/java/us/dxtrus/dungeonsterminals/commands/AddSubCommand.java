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
import us.dxtrus.dungeonsterminals.models.TerminalType;

import java.util.ArrayList;
import java.util.List;

public class AddSubCommand extends BasicSubCommand {
    @Command(name = "add", permission = "terminals.admin")
    public AddSubCommand() {
        super();
    }

    @Override
    public void execute(CommandUser commandUser, String[] strings) {
        Player player = ((BukkitUser) commandUser).getPlayer();
        Config.Commands conf = Config.getInstance().getCommands();
        if (strings.length < 2) {
            commandUser.sendMessage(StringUtils.modernMessage(conf.getInvalidUsageAdd()));
            return;
        }

        String id = strings[0];
        TerminalType type = TerminalType.fromString(strings[1]);

        if (type == null) {
            commandUser.sendMessage(StringUtils.modernMessage(conf.getInvalidType()));
            return;
        }

        MythicPlayer mythicPlayer = MythicDungeons.inst().getMythicPlayer(player);

        if (mythicPlayer.getInstance() == null || !mythicPlayer.getInstance().isEditInstance()) {
            commandUser.sendMessage(StringUtils.modernMessage(conf.getMustBeEditing()));
            return;
        }

        String dungeon = mythicPlayer.getInstance().getDungeon().getFolder().getName();

        Block targetBlock = player.getTargetBlockExact(5);

        if (targetBlock == null) {
            commandUser.sendMessage(StringUtils.modernMessage(conf.getMustBeLookingAtBlock()));
            return;
        }

        if (CacheManager.getInstance().get(targetBlock.getLocation()).isPresent()) {
            commandUser.sendMessage(StringUtils.modernMessage(conf.getTerminalExistsAtThisLoc()));
            return;
        }

        if (CacheManager.getInstance().getAllIds().contains(id)) {
            commandUser.sendMessage(StringUtils.modernMessage(conf.getTerminalExistsWithThisId()));
            return;
        }

        LocRef loc = LocRef.fromLocation(targetBlock.getLocation());

        Terminal terminal = new Terminal(id, type, dungeon, loc);
        CacheManager.getInstance().cache(terminal);
        DatabaseManager.getInstance().save(Terminal.class, terminal)
                .thenRun(() -> commandUser.sendMessage(StringUtils.modernMessage(conf.getTerminalCreated()
                        .formatted(type.getName(), loc.toString(), dungeon))));
    }

    @Override
    public List<String> tabComplete(CommandUser sender, String[] args) {
        if (args.length == 2) {
            List<String> ret = new ArrayList<>();
            for (TerminalType type : TerminalType.values()) {
                ret.add(type.name());
            }
            return ret;
        }
        return List.of();
    }
}
