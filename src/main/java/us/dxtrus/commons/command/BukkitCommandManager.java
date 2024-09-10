package us.dxtrus.commons.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import us.dxtrus.commons.command.user.BukkitUser;
import us.dxtrus.commons.command.user.CommandUser;
import us.dxtrus.commons.command.user.ConsoleUser;
import us.dxtrus.commons.utils.TaskManager;
import us.dxtrus.dungeonsterminals.DungeonsTerminals;
import us.dxtrus.dungeonsterminals.managers.LogManager;

import java.util.*;

public class BukkitCommandManager extends CommandManager {
    private static BukkitCommandManager instance;

    /**
     * Register a new command.
     *
     * @param basicCommand the command.
     */
    @Override
    public void registerCommand(BasicCommand basicCommand) {
        CommandMapUtil.getCommandMap().register("dungeonsterminals", new CommandExecutor(basicCommand));
        loadedCommands.add(basicCommand);
        LogManager.info(String.format("Registered Command %s", basicCommand.getInfo().name()));
    }

    /**
     * Register all commands in the packages.
     *
     * @param packageNames the packages to register the commands in
     */
    public void registerCommands(String... packageNames) {
        Reflections reflections = new Reflections((Object) packageNames);
        Set<Class<? extends BasicCommand>> subTypes = reflections.getSubTypesOf(BasicCommand.class);
        for (Class<? extends BasicCommand> commandClass : subTypes) {
            try {
                BasicCommand command = commandClass.getDeclaredConstructor().newInstance();
                registerCommand(command);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class CommandExecutor extends BukkitCommand {
        private final BasicCommand basicCommand;

        public CommandExecutor(BasicCommand basicCommand) {
            super(basicCommand.getInfo().name());
            this.setAliases(Arrays.asList(basicCommand.getInfo().aliases()));
            if (!basicCommand.getInfo().permission().isEmpty()) {
                this.setPermission(basicCommand.getInfo().permission());
            }
            this.basicCommand = basicCommand;
        }

        @Override
        public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
            if (this.basicCommand.getInfo().inGameOnly() && sender instanceof ConsoleCommandSender) {
                sender.sendMessage("MUST BE PLAYER LOCALE");
                return false;
            }
            if (this.getPermission() != null && !sender.hasPermission(this.getPermission())) {
                sender.sendMessage("NO PERMISSION LOCALE");
                return false;
            }

            CommandUser commandUser = sender instanceof Player player
                    ? BukkitUser.wrap(player)
                    : new ConsoleUser(DungeonsTerminals.getInstance().getAudiences().console());

            if (this.basicCommand.getInfo().async()) {
                TaskManager.runAsync(DungeonsTerminals.getInstance(), () -> basicCommand.execute(commandUser, args));
            } else {
                TaskManager.runSync(DungeonsTerminals.getInstance(), () -> basicCommand.execute(commandUser, args));
            }
            return false;
        }

        @NotNull
        public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
            CommandUser commandUser = sender instanceof Player player
                    ? BukkitUser.wrap(player)
                    : new ConsoleUser(DungeonsTerminals.getInstance().getAudiences().console());

            // Primary argument
            if (args.length <= 1) {
                List<String> completors = basicCommand.tabComplete(commandUser, args);

                if (completors.isEmpty() && !basicCommand.getSubCommands().isEmpty()) {
                    List<String> ret = new ArrayList<>();
                    for (BasicSubCommand subCommand : basicCommand.getSubCommands()) {
                        if (!subCommand.getInfo().permission().isEmpty() && !commandUser.hasPermission(subCommand.getInfo().permission())) {
                            continue;
                        }
                        ret.add(subCommand.getInfo().name());
                        Collections.addAll(ret, subCommand.getInfo().aliases());
                    }
                    if (args.length == 0) {
                        completors.addAll(ret);
                    } else {
                        StringUtil.copyPartialMatches(args[0], ret, completors);
                    }
                    return completors;
                }

                return completors;
            }

            // Sub command tab completer
            List<String> completors = new ArrayList<>();

            List<String> ret = new ArrayList<>();
            for (BasicSubCommand subCommand : basicCommand.getSubCommands()) {
                if (!subCommand.getInfo().name().equals(args[0]) && !Arrays.stream(subCommand.getInfo().aliases()).toList().contains(args[0])) {
                    continue;
                }
                if (!subCommand.getInfo().permission().isEmpty() && !commandUser.hasPermission(subCommand.getInfo().permission())) {
                    continue;
                }
                ret.addAll(subCommand.tabComplete(commandUser, removeFirstElement(args)));
            }
            StringUtil.copyPartialMatches(args[args.length - 1], ret, completors);
            return completors;
        }
    }

    public static BukkitCommandManager getInstance() {
        if (instance == null) {
            instance = new BukkitCommandManager();
        }
        return instance;
    }
}
