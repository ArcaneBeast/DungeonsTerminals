package us.dxtrus.dungeonsterminals.data.persistent;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DatabaseType {
    YAML("YAML");

    private final String friendlyName;
}
