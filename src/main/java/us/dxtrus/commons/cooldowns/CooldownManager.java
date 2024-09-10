package us.dxtrus.commons.cooldowns;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.key.Key;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CooldownManager {
    private static CooldownManager instance;

    private final Map<Key, Cooldown> cooldowns = new HashMap<>();

    public void startCooldown(Cooldown cooldown) {
        cooldowns.put(getKey(cooldown), cooldown);
    }

    public boolean isCooldownActive(Cooldown cooldown) {
        Key key = getKey(cooldown);
        if (!cooldowns.containsKey(key)) {
            return false;
        }

        return System.currentTimeMillis() < cooldowns.get(key).getEndTime();
    }

    public long getRemainingTime(Cooldown cooldown) {
        if (!cooldowns.containsKey(getKey(cooldown))) {
            return 0;
        }

        long endTime = cooldowns.get(getKey(cooldown)).getEndTime();
        long remainingTime = endTime - System.currentTimeMillis();

        return Math.max(remainingTime, 0);
    }

    public void cleanup(Cooldown cooldown) {
        cooldowns.remove(getKey(cooldown));
    }

    private Key getKey(Cooldown cooldown) {
        return Key.key("cooldowns", cooldown.getOwner().toString() + "_" + cooldown.getId().toLowerCase());
    }

    public static CooldownManager getInstance() {
        if (instance == null) {
            instance = new CooldownManager();
        }
        return instance;
    }
}
