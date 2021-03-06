package com.minecraftdimensions.bungeesuitehomes.commands;

import com.minecraftdimensions.bungeesuitehomes.managers.HomesManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetHomeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(args.length==0){
			HomesManager.setHome(sender, "home");
		}else{
			HomesManager.setHome(sender, args[0]);
		}
		return true;
	}

}
