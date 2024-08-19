package us.dxtrus.dungeonsterminals.models;

import org.bukkit.Location;

public class LocRef {
    private final int x;
    private final int y;
    private final int z;

    public LocRef(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static LocRef fromLocation(Location location) {
        return new LocRef(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LocRef otherLoc)) return false;
        return otherLoc.getX() == this.getX() && otherLoc.getY() == this.getY() && otherLoc.getZ() == this.getZ();
    }
}
