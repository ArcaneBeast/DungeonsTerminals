package us.dxtrus.commons.command;

import lombok.Getter;
import us.dxtrus.commons.command.user.CommandUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public abstract class BasicSubCommand {
    private final Command info;

    public BasicSubCommand() {
        this.info = Arrays.stream(this.getClass().getConstructors())
                .filter(method -> method.getAnnotation(Command.class) != null)
                .map(method -> method.getAnnotation(Command.class)).findFirst().orElse(null);

        if (info == null) {
            throw new RuntimeException("BasicSubCommand constructor must be annotated with @Command");
        }
    }

    public abstract void execute(CommandUser sender, String[] args);

    public List<String> tabComplete(CommandUser sender, String[] args) {
        return new ArrayList<>();
    }
}
