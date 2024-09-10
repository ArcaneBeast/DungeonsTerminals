package us.dxtrus.commons.cooldowns;

public enum CooldownType {
    /**
     * Local is stored in server memory, good for single instance game modes.
     */
    LOCAL,
//    /**
//     * Shared is stored in redis but bound to the server group (ex: all of skyblock, or all hubs)
//     * Shared is also stored in server memory to prevent calls to redis if spamming the cooldown call.
//     */
//    SHARED,
//    /**
//     * Global is stored in redis and affects every server on the network (ex: adding cooldown to server switching)
//     * Global is also stored in server memory to prevent calls to redis if spamming the cooldown call.
//     */
//    GLOBAL,
    ;
}
