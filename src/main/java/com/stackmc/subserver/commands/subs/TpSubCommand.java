package com.stackmc.subserver.commands.subs;

import com.stackmc.subserver.SubServer;
import com.stackmc.subserver.instance.Instance;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TpSubCommand implements TabExecutor {

    private final SubServer plugin;
    @Override
    public boolean onCommand(CommandSender sender, Command rootCommand, String label, String[] args) {
        if (sender instanceof Player) {
            if(args.length == 0) {
                sender.sendMessage("§cVous devez préciser un nom de monde.");
                return false;
            }
            World world = Bukkit.getWorld(args[0]);
            if(world != null) {
                Player player = (Player) sender;
                Instance.getInstance(Bukkit.getWorld(args[0])).joinInstance(player);
                return true;
            }
            sender.sendMessage("§cCe monde n'existe pas.");
            return false;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command rootCommand, String label, String[] args) {
        return Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList());
    }
}
