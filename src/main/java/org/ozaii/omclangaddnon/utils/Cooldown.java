package org.ozaii.omclangaddnon.utils;

import java.util.HashMap;
import java.util.Map;

public class Cooldown {

    private static CooldownGlobal instance;
    private Map<String, Long> cooldowns = new HashMap<>();

    private Cooldown() {}

    public static CooldownGlobal getInstance() {
        if (instance == null) {
            instance = new CooldownGlobal();
        }
        return instance;
    }

    public void setCooldown(String playerName, long timeInSeconds) {
        cooldowns.put(playerName, System.currentTimeMillis() + timeInSeconds * 1000);
    }

    public boolean isCooldownActive(String playerName) {
        if (!cooldowns.containsKey(playerName)) {
            return false;
        }
        long cooldownEnd = cooldowns.get(playerName);
        return System.currentTimeMillis() < cooldownEnd;
    }

    public long getRemainingCooldown(String playerName) {
        if (!isCooldownActive(playerName)) {
            return 0;
        }
        return (cooldowns.get(playerName) - System.currentTimeMillis()) / 1000;
    }
}
