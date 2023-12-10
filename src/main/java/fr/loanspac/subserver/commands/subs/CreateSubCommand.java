package fr.loanspac.subserver.commands.subs;

import fr.loanspac.subserver.SubServer;
import lombok.RequiredArgsConstructor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class CreateSubCommand implements TabExecutor {

    private final SubServer plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command rootCommand, String label, String[] args) {
        if(!(args[0].isEmpty())) {
            WorldCreator creator = new WorldCreator(args[0]);
            creator.environment(World.Environment.NORMAL);
            creator.generateStructures(true);
            creator.createWorld();
            sender.sendMessage("Monde créé.");
            return true;
        }
        sender.sendMessage("§cVous devez préciser un nom de monde.");
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command rootCommand, String label, String[] args) {
        return Collections.emptyList();
    }
}
