package us.dxtrus.dungeonsterminals.models;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;

@Getter
public class LocRef {
    private final double x;
    private final double y;
    private final double z;

    public LocRef(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static LocRef fromLocation(Location location) {
        return new LocRef(location.getBlockX() + 0.5, location.getBlockY() + 0.5, location.getBlockZ() + 0.5);
    }

    public Location toBukkit(World world) {
        return new Location(world, x, y, z);
    }

    @Override
    public String toString() {
        return "X %s, Y %s, Z %s".formatted(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LocRef otherLoc)) return false;
        return otherLoc.getX() == this.getX() && otherLoc.getY() == this.getY() && otherLoc.getZ() == this.getZ();
    }

    @Override
    public int hashCode() {
        return Double.hashCode(x) + Double.hashCode(y) + Double.hashCode(z);
    }
}
