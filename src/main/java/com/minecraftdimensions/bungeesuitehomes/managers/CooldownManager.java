package com.minecraftdimensions.bungeesuitehomes.managers;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class CooldownManager {
    private static CooldownManager ourInstance = new CooldownManager();

    public static CooldownManager getInstance() {
        return ourInstance;
    }

    // Map<Type, Map<Permission, Tid>>
    private Map<String, Map<String, Integer>> cooldowns;

    private CooldownManager() {
    }

    public int getCooldown(String type, CommandSender player) {
        if (player instanceof Player) {
            return cooldowns.get(type).entrySet().stream().filter(f -> player.hasPermission(f.getKey())).mapToInt(Map.Entry::getValue).min().orElse(0);
        }
        return 0;
    }

    public void setCooldowns(Map<String, Map<String, Integer>> cooldowns) {
        this.cooldowns = cooldowns;
    }
}
