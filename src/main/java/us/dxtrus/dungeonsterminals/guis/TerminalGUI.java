package us.dxtrus.dungeonsterminals.guis;

import us.dxtrus.commons.gui.FastInv;
import us.dxtrus.dungeonsterminals.models.TerminalType;

public abstract class TerminalGUI extends FastInv {
    public TerminalGUI(TerminalType type) {
        super(type.getGuiSize(), type.getFriendlyName());
    }

    protected abstract void completeTerminal();
}
