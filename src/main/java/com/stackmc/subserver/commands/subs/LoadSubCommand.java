package com.stackmc.subserver.commands.subs;

import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.stackmc.subserver.SubServer;
import com.stackmc.subserver.instance.Instance;
import com.stackmc.subserver.worldgen.SWMUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class LoadSubCommand implements TabExecutor {

    private final SubServer plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command rootCommand, String label, String[] args) {
        if(args.length == 0) {
            sender.sendMessage("§cVous devez préciser un nom d'instance.");
            return false;
        }
        if(args.length < 2) {
            sender.sendMessage("§cVous devez préciser au moins un monde à charger.");
            return false;
        }

        Instance instance = new Instance(args[0], plugin);

        for(int i = 1; i != args.length; i++) {
            String worldName = args[i];
            long startTime = System.currentTimeMillis();

            try {
                SWMUtils.loadWorld(worldName);
            } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException | WorldInUseException ex) {
                sender.sendMessage("§cUne erreur est survenue lors du chargement du monde " + worldName + ".");
                return false;
            }

            long totalTime = System.currentTimeMillis() - startTime;
            sender.sendMessage("Monde " + worldName +  " chargé en " + totalTime + "ms ou " + ((float) totalTime / 50f) + " ticks .");
        }

        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                for(int i = 1; i != args.length; i++) {
                    World world = Bukkit.getWorld(args[i]);
                    if (world == null) {
                        Bukkit.getScheduler().runTaskLater(plugin, this, 20);
                        return;
                    }
                    instance.addWorld(world);
                }
            }
        }, 20);

        instance.register();
        sender.sendMessage("Instance chargé.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command rootCommand, String label, String[] args) {
        return Collections.emptyList(); // ça doit être possible de trouver les noms de maps déjà existants en checkant le dossier
    }
}
