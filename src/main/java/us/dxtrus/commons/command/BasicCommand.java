package us.dxtrus.commons.command;

import lombok.Getter;
import us.dxtrus.commons.command.user.CommandUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public abstract class BasicCommand {
    private final Command info;
    private final List<BasicSubCommand> subCommands;

    public BasicCommand() {
        this.info = Arrays.stream(this.getClass().getConstructors()).filter(method -> method.getAnnotation(Command.class) != null).map(method -> method.getAnnotation(Command.class)).findFirst().orElse(null);
        this.subCommands = new ArrayList<>();

        if (info == null) {
            throw new RuntimeException("BasicCommand constructor must be annotated with @Command");
        }
    }

    public abstract void execute(CommandUser sender, String[] args);

    public List<String> tabComplete(CommandUser sender, String[] args) {
        return new ArrayList<>();
    }

    /**
     * Handles subcommand execution easily.
     * @param sender command sender
     * @param args command args
     * @return true if the subcommand was executed,
     * false if the sender does not have permission or if the sender was console on a player only command
     */
    public abstract boolean subCommandExecutor(CommandUser sender, String[] args);

    protected String[] removeFirstElement(String[] array) {
        if (array == null || array.length == 0) {
            return new String[]{};
        }

        String[] newArray = new String[array.length - 1];
        System.arraycopy(array, 1, newArray, 0, array.length - 1);

        return newArray;
    }
}