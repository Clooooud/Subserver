package fr.loanspac.subserver.commands.subs;

import fr.loanspac.subserver.SubServer;
import lombok.RequiredArgsConstructor;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class LoadSubCommand implements TabExecutor {

    private final SubServer plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command rootCommand, String label, String[] args) {
        if(!(args[0].isEmpty())) {
            WorldCreator creator = new WorldCreator(args[0]);
            creator.createWorld();
            sender.sendMessage("Monde chargé.");
            return true;
        }
        sender.sendMessage("§cVous devez préciser un nom de monde.");
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command rootCommand, String label, String[] args) {
        return Collections.emptyList(); // ça doit être possible de trouver les noms de maps déjà existants en checkant le dossier
    }
}
