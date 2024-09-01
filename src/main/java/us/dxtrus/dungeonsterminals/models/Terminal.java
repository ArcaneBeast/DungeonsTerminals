package us.dxtrus.dungeonsterminals.models;

import lombok.Getter;
import us.dxtrus.commons.database.DatabaseObject;

@Getter
public class Terminal implements DatabaseObject {
    private final String id;
    private final TerminalType type;
    private final String associatedDungeon;
    private final LocRef location;

    public Terminal(String id, TerminalType type, String associatedDungeon, LocRef location) {
        this.id = id;
        this.type = type;
        this.associatedDungeon = associatedDungeon;
        this.location = location;
    }

}
