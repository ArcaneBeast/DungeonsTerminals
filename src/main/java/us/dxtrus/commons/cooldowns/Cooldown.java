package us.dxtrus.commons.cooldowns;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Cooldown {
    private final String id;
    private final UUID owner;
    private final CooldownType type;
    private final long length;
    private long endTime = -1;

    private Cooldown(String id, UUID owner, CooldownType type, long length) {
        this.id = id;
        this.owner = owner;
        this.type = type;
        this.length = length;
    }

    public void start() {
        this.endTime = System.currentTimeMillis() + length;
        CooldownManager.getInstance().startCooldown(this);
    }

    public boolean isActive() {
        return CooldownManager.getInstance().isCooldownActive(this);
    }

    /**
     * Get the time that the cooldown has left in it
     *
     * @return time left in millis
     */
    public long remainingTime() {
        return CooldownManager.getInstance().getRemainingTime(this);
    }

    public void cleanup() {
        CooldownManager.getInstance().cleanup(this);
    }

    /**
     * Executes the cooldown logic. with a callback via a functional interface.
     */
    public void execute(CooldownExecution execution) {
        if (isActive()) {
            execution.callback(false, remainingTime());
            return;
        }
        cleanup();
        execution.callback(true, -1L);
        start();
    }

    /**
     * Executes the cooldown logic. returning the values as a tuple
     */
    public CooldownReponse execute() {
        if (isActive()) {
            return new CooldownReponse(false, remainingTime());
        }
        cleanup();
        start();
        return new CooldownReponse(true, -1L);
    }

    /**
     * Make a local cooldown with a custom id.
     *
     * @param id the id of the cooldown.
     * @param player the player who the cooldown belongs to.
     * @param length time in milliseconds.
     */
    public static Cooldown local(String id, UUID player, long length) {
        return new Cooldown(id, player, CooldownType.LOCAL, length);
    }

//    /**
//     * Make a shared cooldown with a custom id.
//     *
//     * @param id the id of the cooldown.
//     * @param player the player who the cooldown belongs to.
//     * @param length time in milliseconds.
//     */
//    public static Cooldown shared(String id, UUID player, long length) {
//        return new Cooldown(id, player, CooldownType.SHARED, length);
//    }

//    /**
//     * Make a global cooldown with a custom id.
//     *
//     * @param id the id of the cooldown.
//     * @param player the player who the cooldown belongs to.
//     * @param length time in milliseconds.
//     */
//    public static Cooldown global(String id, UUID player, long length) {
//        return new Cooldown(id, player, CooldownType.GLOBAL, length);
//    }

    /**
     * Make a local cooldown that belongs to a class.
     *
     * @param clazz the class the cooldown belongs to.
     * @param player the player who the cooldown belongs to.
     * @param length time in milliseconds.
     */
    public static Cooldown localFromClass(Class<?> clazz, UUID player, long length) {
        return new Cooldown(clazz.getSimpleName(), player, CooldownType.LOCAL, length);
    }

//    /**
//     * Make a shared cooldown that belongs to a class.
//     *
//     * @param clazz the class the cooldown belongs to.
//     * @param player the player who the cooldown belongs to.
//     * @param length time in milliseconds.
//     */
//    public static Cooldown sharedFromClass(Class<?> clazz, UUID player, long length) {
//        return new Cooldown(clazz.getSimpleName(), player, CooldownType.SHARED, length);
//    }
//
//    /**
//     * Make a global cooldown that belongs to a class.
//     *
//     * @param clazz the class the cooldown belongs to.
//     * @param player the player who the cooldown belongs to.
//     * @param length time in milliseconds.
//     */
//    public static Cooldown globalFromClass(Class<?> clazz, UUID player, long length) {
//        return new Cooldown(clazz.getSimpleName(), player, CooldownType.GLOBAL, length);
//    }

    @FunctionalInterface
    public interface CooldownExecution {
        /**
         * Returns info about the execution.
         *
         * @param shouldContinue false if the cooldown is active. true if the cooldown is not active but just started.
         * @param timeLeft the amount of time left in the cooldown in millis, -1 if the cooldown is not active
         */
        void callback(boolean shouldContinue, long timeLeft);
    }
}
