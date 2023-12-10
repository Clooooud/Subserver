package fr.loanspac.subserver.commands.subs;

import fr.loanspac.subserver.SubServer;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TpSubCommand implements TabExecutor {

    private final SubServer plugin;
    @Override
    public boolean onCommand(CommandSender sender, Command rootCommand, String label, String[] args) {
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

    @Override
    public List<String> onTabComplete(CommandSender sender, Command rootCommand, String label, String[] args) {
        return Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList());
    }
}
