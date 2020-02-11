package com.minecraftdimensions.bungeesuitehomes.managers;

import com.minecraftdimensions.bungeesuitehomes.BungeeSuiteHomes;
import org.bukkit.entity.Player;

public class PermissionsManager {

    public static void addAllPermissions( Player player ) {
        player.addAttachment( BungeeSuiteHomes.instance, "bungeesuite.homes.*", true );
    }

    public static void addAdminPermissions( Player player ) {
        player.addAttachment( BungeeSuiteHomes.instance, "bungeesuite.homes.admin", true );
    }

    public static void addUserPermissions( Player player ) {
        player.addAttachment( BungeeSuiteHomes.instance, "bungeesuite.homes.user", true );
    }
}
