package com.stackmc.subserver.commands.subs;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.*;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import com.stackmc.subserver.SubServer;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
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
        long startTime = System.currentTimeMillis();
        if(!(args[0].isEmpty())) {
            SlimePlugin plugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");

            String worldName = args[0];
            SlimeLoader loader = plugin.getLoader("file");
            SlimePropertyMap properties = new SlimePropertyMap();

            properties.setString(SlimeProperties.DIFFICULTY, "normal");
            properties.setInt(SlimeProperties.SPAWN_X, 123);
            properties.setInt(SlimeProperties.SPAWN_Y, 112);
            properties.setInt(SlimeProperties.SPAWN_Z, 170);

            try {
                // Note that this method should be called asynchronously
                SlimeWorld world = plugin.loadWorld(loader, worldName, false, properties);

                // This method must be called synchronously
                plugin.generateWorld(world);
            } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException | WorldInUseException ex) {
                sender.sendMessage("§cUne erreur est survenue lors du chargement du monde.");
                return false;
            }

            long totalTime = System.currentTimeMillis() - startTime;
            sender.sendMessage("Monde chargé en " + totalTime + "ms ou " + ((float) totalTime / 50f) + " ticks .");
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
