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
        if(args[0].isEmpty()) {
            sender.sendMessage("§cVous devez préciser un nom de monde.");
            return false;
        }

        String worldName = args[0];
        long startTime = System.currentTimeMillis();

        try {
            SWMUtils.loadWorld(worldName);
        } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException | WorldInUseException ex) {
            sender.sendMessage("§cUne erreur est survenue lors du chargement du monde.");
            return false;
        }

        long totalTime = System.currentTimeMillis() - startTime;
        sender.sendMessage("Monde chargé en " + totalTime + "ms ou " + ((float) totalTime / 50f) + " ticks .");

        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                World world = Bukkit.getWorld(args[0]);
                if (world == null) {
                    Bukkit.getScheduler().runTaskLater(plugin, this, 20);
                    return;
                }

                Instance instance = new Instance(args[0], plugin);
                instance.addWorld(world);
                instance.register();
            }
        }, 20);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command rootCommand, String label, String[] args) {
        return Collections.emptyList(); // ça doit être possible de trouver les noms de maps déjà existants en checkant le dossier
    }
}
