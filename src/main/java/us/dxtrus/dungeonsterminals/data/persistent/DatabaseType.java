package us.dxtrus.dungeonsterminals.data.persistent;

public enum DatabaseType {
    YAML("YAML")
    ;

    private final String friendlyName;


    DatabaseType(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return friendlyName;
    }
}
