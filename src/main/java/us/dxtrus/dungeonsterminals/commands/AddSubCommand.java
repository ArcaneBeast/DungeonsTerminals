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
        Player player = ((BukkitUser) commandUser).getAudience();
        if (strings.length < 2) {
            commandUser.sendMessage(StringUtils.modernMessage("&cUsage: /terminals add <id> <type>"));
            return;
        }

        String id = strings[0];
        TerminalType type = TerminalType.fromString(strings[1]);

        if (type == null) {
            commandUser.sendMessage(StringUtils.modernMessage("&cInvalid Type!"));
            return;
        }

        MythicPlayer mythicPlayer = MythicDungeons.inst().getMythicPlayer(player);

        if (mythicPlayer.getInstance() == null || !mythicPlayer.getInstance().isEditMode()) {
            commandUser.sendMessage(StringUtils.modernMessage("&cYou must be editing a dungeon!"));
            return;
        }

        String dungeon = mythicPlayer.getInstance().getDungeon().getFolder().getName();

        Block targetBlock = player.getTargetBlockExact(5);

        if (targetBlock == null) {
            commandUser.sendMessage(StringUtils.modernMessage("&cYou must be looking at a block!"));
            return;
        }

        if (CacheManager.getInstance().get(targetBlock.getLocation()).isPresent()) {
            commandUser.sendMessage(StringUtils.modernMessage("&cThere is already a terminal at this location!"));
            return;
        }

        if (CacheManager.getInstance().getAllIds().contains(id)) {
            commandUser.sendMessage(StringUtils.modernMessage("&cThere is already a terminal with this ID!"));
            return;
        }

        LocRef loc = LocRef.fromLocation(targetBlock.getLocation());

        if (CacheManager.getInstance().get(loc).isPresent()) {
            commandUser.sendMessage(StringUtils.modernMessage("&cThere is already a terminal at this location!"));
            return;
        }

        Terminal terminal = new Terminal(id, type, dungeon, loc);
        CacheManager.getInstance().cache(terminal);
        DatabaseManager.getInstance().save(Terminal.class, terminal)
                .thenRun(() -> commandUser.sendMessage(StringUtils.modernMessage("&aCreated %s terminal at %s in dungeon %s"
                        .formatted(type.getFriendlyName(), loc.toString(), dungeon))));
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
