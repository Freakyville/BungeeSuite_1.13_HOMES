package com.minecraftdimensions.bungeesuitehomes.commands;

import com.minecraftdimensions.bungeesuitehomes.managers.HomesManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DelHomeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(args.length>0){
			HomesManager.deleteHome(sender, args[0]);
		}else{
			HomesManager.deleteHome(sender, "home");
		}
		return true;
	}

}
