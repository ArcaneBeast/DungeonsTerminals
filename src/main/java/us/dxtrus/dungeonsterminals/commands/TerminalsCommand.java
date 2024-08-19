package us.dxtrus.dungeonsterminals.commands;

import org.bukkit.plugin.java.JavaPlugin;
import us.dxtrus.commons.command.BukkitCommand;
import us.dxtrus.commons.command.Command;
import us.dxtrus.commons.command.user.CommandUser;
import us.dxtrus.commons.utils.StringUtils;

public class TerminalsCommand extends BukkitCommand {
    @Command(name = "terminals", permission = "terminals.admin")
    public TerminalsCommand(JavaPlugin plugin) {
        super(plugin);
        getSubCommands().add(new AddSubCommand());
        getSubCommands().add(new RemoveSubCommand());
    }

    @Override
    public void execute(CommandUser commandUser, String[] strings) {
        if (strings.length >= 1) {
            if (subCommandExecutor(commandUser, strings)) return;
            commandUser.sendMessage(StringUtils.modernMessage("&cUsage: &f/terminals <add|remove>"));
            return;
        }

        commandUser.sendMessage(StringUtils.modernMessage("&cUsage: &f/terminals <add|remove>"));
    }
}
