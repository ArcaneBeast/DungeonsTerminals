package us.dxtrus.dungeonsterminals.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import us.dxtrus.dungeonsterminals.config.Config;

@Getter
@AllArgsConstructor
public enum TerminalType {
    MEMORIZE(Config.getInstance().getTerminals().getMemorize().getTitle(),
            Config.getInstance().getTerminals().getMemorize().getName(), 3*9), // remember what items got displayed
//    SNAKE(Config.getInstance().getTerminals().getSnake().getTitle(),
//            Config.getInstance().getTerminals().getSnake().getName(), 5*9), // click on the tiles to connect 2 sides
    SWITCHES(Config.getInstance().getTerminals().getSwitches().getTitle(),
            Config.getInstance().getTerminals().getSwitches().getName(), 3*9), // turn all the switches to on
    ;

    private final String title;
    private final String name;
    private final int guiSize;

    public static TerminalType fromString(String str) {
        try {
            return valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
