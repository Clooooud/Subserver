package com.stackmc.subserver.commands.subs;

import com.stackmc.subserver.SubServer;
import com.stackmc.subserver.instance.Instance;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class ListSubCommand implements TabExecutor {

    private final SubServer plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command rootCommand, String label, String[] args) {
        final String[] message = {"Voici la liste des instances :\n"};
        Instance.getInstances().forEach(instance -> {
            final String[] worlds = {""};
            instance.worlds.forEach(world -> {
                if(instance.worlds.size() > 1) {
                    worlds[0] = worlds[0] + world.getName() + ", ";
                } else {
                    worlds[0] = worlds[0] + world.getName();
                }
            });

            message[0] = message[0] + instance.name + " (" + worlds[0] + ")\n";
        });
        sender.sendMessage(message[0]);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command rootCommand, String label, String[] args) {
        return Collections.emptyList(); // ça doit être possible de trouver les noms de maps déjà existants en checkant le dossier
    }

}
