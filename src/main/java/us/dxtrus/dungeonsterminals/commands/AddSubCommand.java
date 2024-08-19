package us.dxtrus.dungeonsterminals.commands;

import us.dxtrus.commons.command.BasicSubCommand;
import us.dxtrus.commons.command.Command;
import us.dxtrus.commons.command.user.CommandUser;

public class AddSubCommand extends BasicSubCommand {
    @Command(name = "add", permission = "terminals.admin")
    public AddSubCommand() {
        super();
    }

    @Override
    public void execute(CommandUser commandUser, String[] strings) {
        commandUser.sendMessage("setting <type> terminal for dungeon <current_editing> at location <loc>");
    }
}
