package us.dxtrus.dungeonsterminals.commands;

import us.dxtrus.commons.command.BasicSubCommand;
import us.dxtrus.commons.command.Command;
import us.dxtrus.commons.command.user.CommandUser;

public class RemoveSubCommand extends BasicSubCommand {
    @Command(name = "remove", permission = "terminals.admin")
    public RemoveSubCommand() {
        super();
    }

    @Override
    public void execute(CommandUser commandUser, String[] strings) {
        commandUser.sendMessage("removing <type> terminal for dungeon <current_editing> at location <loc>");
    }
}
