package com.minecraftdimensions.bungeesuitehomes;

import com.minecraftdimensions.bungeesuitehomes.commands.*;
import com.minecraftdimensions.bungeesuitehomes.listeners.HomesListener;
import com.minecraftdimensions.bungeesuitehomes.listeners.HomesMessageListener;
import com.minecraftdimensions.bungeesuitehomes.managers.CooldownManager;
import com.minecraftdimensions.bungeesuitehomes.redis.RedisManager;
import com.minecraftdimensions.bungeesuiteteleports.BungeeSuiteTeleports;
import io.github.freakyville.utils.config.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class BungeeSuiteHomes extends JavaPlugin {

	public static BungeeSuiteHomes instance;

	public static String OUTGOING_PLUGIN_CHANNEL = "bsuite:homes-in";
	static String INCOMING_PLUGIN_CHANNEL = "bsuite:homes-out";
	public static boolean usingTeleports = false;
    public static String server;

	@Override
	public void onEnable() {
		instance = this;
		registerListeners();
		registerChannels();
		registerCommands();
		BungeeSuiteTeleports bt = (BungeeSuiteTeleports) Bukkit.getPluginManager().getPlugin("Teleports");
		if(bt!=null){
			if(bt.getDescription().getAuthors().contains("Bloodsplat")){
				usingTeleports = true;
			}
		}

        ConfigHandler configHandler = new ConfigHandler(instance, "config.yml");

        server = configHandler.getString("server");
        Map<String, Map<String, Integer>> cooldowns = new HashMap<>();
        for (String command : configHandler.getConfigSection("cooldowns").getKeys(false)) {
            cooldowns.putIfAbsent(command, new HashMap<>());
            for (String perm : configHandler.getConfigSection("cooldowns." + command).getKeys(false)) {
                cooldowns.get(command).put(perm, configHandler.getInt("cooldowns." + command + "." + perm));
            }
        }
        CooldownManager.getInstance().setCooldowns(cooldowns);

        RedisManager.getInstance().init(configHandler.getString("host"), configHandler.getString("password"), configHandler.getInt("port"), configHandler.getInt("timeout"));

    }
	
	private void registerCommands() {
		getCommand("sethome").setExecutor(new SetHomeCommand());
		getCommand("home").setExecutor(new HomeCommand());
		getCommand("delhome").setExecutor(new DelHomeCommand());
		getCommand("homes").setExecutor(new HomesCommand());
		getCommand("importhomes").setExecutor(new ImportHomesCommand());
	}

	private void registerChannels() {
		Bukkit.getMessenger().registerIncomingPluginChannel(this,
				INCOMING_PLUGIN_CHANNEL, new HomesMessageListener());
		Bukkit.getMessenger().registerOutgoingPluginChannel(this,
				OUTGOING_PLUGIN_CHANNEL);
	}

	private void registerListeners() {
		getServer().getPluginManager().registerEvents(
				new HomesListener(), this);
	}


}
