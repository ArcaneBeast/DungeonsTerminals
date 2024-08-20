package us.dxtrus.dungeonsterminals.data.persistant;

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
