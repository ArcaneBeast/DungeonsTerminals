package us.dxtrus.dungeonsterminals.models;

public enum TerminalType {
    MEMORIZE("Memorize the Items", 3*9), // remember what items got displayed
    SNAKE("Follow the Path", 5*9), // click on the tiles to connect 2 sides
    SWITCHES("Turn all the switches on", 3*9), // turn all the switches to on
    ;

    private final String friendlyName;
    private final int guiSize;

    TerminalType(String friendlyName, int guiSize) {
        this.friendlyName = friendlyName;
        this.guiSize = guiSize;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public int getGuiSize() {
        return guiSize;
    }
}
