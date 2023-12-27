package com.stackmc.subserver.commands.subs;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.WorldAlreadyExistsException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import com.stackmc.subserver.SubServer;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class CreateSubCommand implements TabExecutor {

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
            properties.setInt(SlimeProperties.SPAWN_X, 0);
            properties.setInt(SlimeProperties.SPAWN_Y, 100);
            properties.setInt(SlimeProperties.SPAWN_Z, 0);

            try {
                // Note that this method should be called asynchronously
                plugin.createEmptyWorld(loader, worldName, false, properties);
            } catch (WorldAlreadyExistsException | IOException ex) {
                sender.sendMessage("§cUne erreur est survenue lors de la création du monde.");
                return false;
            }

            long totalTime = System.currentTimeMillis() - startTime;
            sender.sendMessage("Monde créé en " + totalTime + "ms ou " + ((float) totalTime / 50f) + " ticks .");
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