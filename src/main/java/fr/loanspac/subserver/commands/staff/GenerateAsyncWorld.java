package fr.loanspac.subserver.commands.staff;

import fr.loanspac.subserver.SubServer;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class GenerateAsyncWorld implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(cmd.getName().equalsIgnoreCase("aworld")) {
            if(args[0].equals("create")) {
                if(!(args[1].isEmpty())) {
                    WorldCreator creator = new WorldCreator(args[1]);
                    Bukkit.getScheduler().runTaskAsynchronously(SubServer.instance(), () -> {
                        creator.environment(World.Environment.NORMAL);
                        creator.generateStructures(true);
                    });
                    creator.createWorld();
                    sender.sendMessage("Monde créé.");
                    return true;
                }
                sender.sendMessage("§cVous devez préciser un nom de monde.");
                return false;
            }
            if(args[0].equals("load")) {
                if(!(args[1].isEmpty())) {
                    WorldCreator creator = new WorldCreator(args[1]);
                    creator.createWorld();
                    sender.sendMessage("Monde chargé.");
                    return true;
                }
                sender.sendMessage("§cVous devez préciser un nom de monde.");
                return false;
            }
            sender.sendMessage("§cVous devez utiliser <create> ou <load> pour un monde.");
            return false;
        }

        if(cmd.getName().equalsIgnoreCase("worldtp")) {
            if (sender instanceof Player) {
                if(!(args[0].isEmpty())) {
                    ((Player) sender).teleport(Objects.requireNonNull(Bukkit.getWorld(args[0])).getSpawnLocation());
                    return true;
                }
                sender.sendMessage("§cVous devez préciser un nom de monde.");
                return false;
            }
            return false;
        }
        return false;
    }
}
