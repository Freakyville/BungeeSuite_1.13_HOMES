package com.minecraftdimensions.bungeesuitehomes.managers;

import com.minecraftdimensions.bungeesuitehomes.BungeeSuiteHomes;
import com.minecraftdimensions.bungeesuitehomes.tasks.PluginMessageTask;
import com.minecraftdimensions.bungeesuiteteleports.managers.TeleportsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.HashMap;
import java.util.Set;

public class HomesManager {

    public static HashMap<String, Location> pendingTeleports = new HashMap<>();

    public static void deleteHome( CommandSender sender, String home ) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream( b );
        try {
            out.writeUTF( "deletehome" );
            out.writeUTF( sender.getName() );
            out.writeUTF( home );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        new PluginMessageTask( b ).runTaskAsynchronously( BungeeSuiteHomes.instance );

    }

    public static void sendHome( CommandSender sender, String home ) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream( b );
        try {
            out.writeUTF( "sendplayerhome" );
            out.writeUTF( sender.getName() );
            out.writeUTF( home );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        new PluginMessageTask( b ).runTaskAsynchronously( BungeeSuiteHomes.instance );

    }

    public static void getHomesList( CommandSender sender ) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream( b );
        try {
            out.writeUTF( "gethomeslist" );
            out.writeUTF( sender.getName() );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        new PluginMessageTask( b ).runTaskAsynchronously( BungeeSuiteHomes.instance );

    }

    public static void importHomes( CommandSender sender ) {
        String path = "plugins/Essentials/userdata";
        File folder = new File( path );
        File[] listOfFiles = folder.listFiles( new FilenameFilter() {
            public boolean accept( File dir, String name ) {
                return name.toLowerCase().endsWith( ".yml" );
            }
        } );
        int userCount = 0;
        int userHomeCount = 0;
        int homeCount = 0;
        for ( File data : listOfFiles ) {
            userCount++;
            FileConfiguration user = YamlConfiguration.loadConfiguration( data );
            if ( user.contains( "homes" ) ) {
                userHomeCount++;
                String username = user.getString( "lastAccountName" );
                Set<String> homedata = user.getConfigurationSection( "homes" ).getKeys( false );
                if ( homedata != null ) {
                    for ( String homes : homedata ) {
                    	if (Bukkit.getWorld( user.getString( "homes." + homes + ".world" ) ) != null) {
                            Location loc = new Location( Bukkit.getWorld( user.getString( "homes." + homes + ".world" ) ), user.getDouble( "homes." + homes + ".x" ), user.getDouble( "homes." + homes + ".y" ), user.getDouble( "homes." + homes + ".z" ), ( float ) user.getDouble( "homes." + homes + ".yaw" ), ( float ) user.getDouble( "homes." + homes + ".pitch" ) );
                            if (loc.getWorld().getName() != null) {
                                setHome( username, homes, loc );
                                homeCount++;
                            }
                    	}
                    }
                }
            }
        }
        sender.sendMessage( ChatColor.GOLD + "Out of " + userCount + "users, " + userHomeCount + " had homes" );
        sender.sendMessage( ChatColor.GOLD + "Homes imported: " + homeCount );

    }

    public static void reloadHomes( CommandSender sender ) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream( b );
        try {
            out.writeUTF( "reloadhomes" );
            out.writeUTF( sender.getName() );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        new PluginMessageTask( b ).runTaskAsynchronously( BungeeSuiteHomes.instance );

    }

    public static void setHome( CommandSender sender, String home ) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream( b );
        Location l = ( ( Player ) sender ).getLocation();
        try {
            out.writeUTF( "setplayershome" );
            out.writeUTF( sender.getName() );
            out.writeInt( getServerHomesLimit( sender ) );
            out.writeInt( getGlobalHomesLimit( sender ) );
            out.writeUTF( home );
            out.writeUTF( l.getWorld().getName() );
            out.writeDouble( l.getX() );
            out.writeDouble( l.getY() );
            out.writeDouble( l.getZ() );
            out.writeFloat( l.getYaw() );
            out.writeFloat( l.getPitch() );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        new PluginMessageTask( b ).runTaskAsynchronously( BungeeSuiteHomes.instance );

    }

    public static void setHome( String sender, String home, Location l ) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream( b );
        try {
            out.writeUTF( "setplayershome" );
            out.writeUTF( sender );
            out.writeInt( 900 );
            out.writeInt( 1000 );
            out.writeUTF( home );
            out.writeUTF( l.getWorld().getName() );
            out.writeDouble( l.getX() );
            out.writeDouble( l.getY() );
            out.writeDouble( l.getZ() );
            out.writeFloat( l.getYaw() );
            out.writeFloat( l.getPitch() );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        new PluginMessageTask( b ).runTaskAsynchronously( BungeeSuiteHomes.instance );


    }

    public static int getServerHomesLimit( CommandSender p ) {
        int max = 0;

        int maxlimit = 100;

        if ( p.hasPermission( "bungeesuite.homes.limits.server.*" ) || p.hasPermission( "bungeesuite.homes.limits.*" ) ) {
            return maxlimit;
        } else {
            for ( int ctr = 0; ctr < maxlimit; ctr++ ) {
                if ( p.hasPermission( "bungeesuite.homes.limits.server." + ctr ) ) {
                    max = ctr;
                }
            }

        }

        return max;
    }

    public static int getGlobalHomesLimit( CommandSender p ) {
        int max = 0;

        int maxlimit = 300;

        if ( p.hasPermission( "bungeesuite.homes.limits.global.*" ) || p.hasPermission( "bungeesuite.homes.limits.*" ) ) {
            return maxlimit;
        } else {
            for ( int ctr = 0; ctr < maxlimit; ctr++ ) {
                if ( p.hasPermission( "bungeesuite.homes.limits.global." + ctr ) ) {
                    max = ctr;
                }
            }

        }
        return max;
    }

    public static void teleportPlayerToLocation( final String player, Location location ) {
        Player p = Bukkit.getPlayer( player );
        if ( p != null ) {
            p.teleport( location );
        } else {
            pendingTeleports.put( player, location );
            if ( BungeeSuiteHomes.usingTeleports ) {
                TeleportsManager.ignoreTeleport.add( p );
            }
            Bukkit.getScheduler().runTaskLaterAsynchronously( BungeeSuiteHomes.instance, new Runnable() {
                @Override
                public void run() {
                    if ( pendingTeleports.containsKey( player ) ) {
                        pendingTeleports.remove( player );
                    }
                }
            }, 100 );
        }

    }

    public static void sendVersion() {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream( b );
        try {
            out.writeUTF( "sendversion" );
            out.writeUTF( ChatColor.RED + "Homes - " + ChatColor.GOLD + BungeeSuiteHomes.instance.getDescription().getVersion() );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        new PluginMessageTask( b ).runTaskAsynchronously( BungeeSuiteHomes.instance );


    }
}
