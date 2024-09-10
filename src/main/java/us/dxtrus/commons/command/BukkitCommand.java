package us.dxtrus.commons.command;

import org.bukkit.plugin.java.JavaPlugin;
import us.dxtrus.commons.command.user.CommandUser;
import us.dxtrus.commons.command.user.ConsoleUser;

import java.util.Arrays;

public abstract class BukkitCommand extends BasicCommand {
    private final JavaPlugin plugin;

    public BukkitCommand(JavaPlugin plugin) {
        super();
        this.plugin = plugin;
    }

    /**
     * Handles subcommand execution easily.
     *
     * @param sender command sender
     * @param args   command args
     * @return true if the subcommand was executed,
     * false if the sender does not have permission or if the sender was console on a player only command
     */
    @Override
    public boolean subCommandExecutor(CommandUser sender, String[] args) {
        for (BasicSubCommand subCommand : getSubCommands()) {
            if (args[0].equalsIgnoreCase(subCommand.getInfo().name())
                    || Arrays.stream(subCommand.getInfo().aliases()).toList().contains(args[0])) {

                if (subCommand.getInfo().inGameOnly() && sender instanceof ConsoleUser) {
                    sender.sendMessage("MUST BE PLAYER LOCALE");
                    return false;
                }

                if (!subCommand.getInfo().permission().isEmpty() && !sender.hasPermission(subCommand.getInfo().permission())) {
                    sender.sendMessage("NO PERMISSION LOCALE");
                    return false;
                }

                if (subCommand.getInfo().async()) {
                    plugin.getServer().getScheduler().runTaskAsynchronously(plugin,
                            () -> subCommand.execute(sender, removeFirstElement(args)));
                } else {
                    plugin.getServer().getScheduler().runTask(plugin,
                            () -> subCommand.execute(sender, removeFirstElement(args)));
                }
                return true;
            }
        }
        return false;
    }
}
