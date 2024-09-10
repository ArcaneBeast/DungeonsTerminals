package us.dxtrus.commons.command;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class CommandManager {
    protected final List<BasicCommand> loadedCommands = new ArrayList<>();

    /**
     * Register a new command.
     * @param basicCommand the command.
     */
    public abstract void registerCommand(BasicCommand basicCommand);

    /**
     * Remove the first element of the args array.
     * @param array args
     * @return args - 1st element
     */
    protected String[] removeFirstElement(String[] array) {
        if (array == null || array.length == 0) {
            return new String[]{};
        }

        String[] newArray = new String[array.length - 1];
        System.arraycopy(array, 1, newArray, 0, array.length - 1);

        return newArray;
    }
}