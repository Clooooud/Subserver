package com.stackmc.subserver.commands.subs;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.*;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import com.stackmc.subserver.SubServer;
import com.stackmc.subserver.instance.Instance;
import com.stackmc.subserver.listeners.WorldInitListener;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class LoadSubCommand implements TabExecutor {

    private final SubServer plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command rootCommand, String label, String[] args) {
        long startTime = System.currentTimeMillis();
        if(!(args[0].isEmpty())) {
            SlimePlugin slime_plugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");

            String worldName = args[0];
            SlimeLoader loader = slime_plugin.getLoader("file");
            SlimePropertyMap properties = new SlimePropertyMap();

            properties.setString(SlimeProperties.DIFFICULTY, "normal");
            properties.setInt(SlimeProperties.SPAWN_X, 0);
            properties.setInt(SlimeProperties.SPAWN_Y, 100);
            properties.setInt(SlimeProperties.SPAWN_Z, 0);

            try {
                // Note that this method should be called asynchronously
                SlimeWorld world = slime_plugin.loadWorld(loader, worldName, false, properties);

                // This method must be called synchronously
                slime_plugin.generateWorld(world);
            } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException | WorldInUseException ex) {
                sender.sendMessage("§cUne erreur est survenue lors du chargement du monde.");
                return false;
            }

            long totalTime = System.currentTimeMillis() - startTime;
            sender.sendMessage("Monde chargé en " + totalTime + "ms ou " + ((float) totalTime / 50f) + " ticks .");

            List<World> worlds = new ArrayList<>();

            /*
            Bukkit.getScheduler().runTaskLater(plugin, new BukkitRunnable() {
                @Override
                public void run() {
                    worlds.add(Bukkit.getWorld(args[0]));
                    plugin.getInstances().add(new Instance(args[0], worlds));
                }
            }, 200);
             */

            new BukkitRunnable() {
                @Override
                public void run() {
                    World world = Bukkit.getWorld(args[0]);
                    if (world == null) {
                        this.runTaskLater(...);
                        return;
                    }
                    worlds.add(world);
                    plugin.getInstances().add(new Instance(args[0], worlds));
                }
            };

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
