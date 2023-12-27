package com.stackmc.subserver.worldgen;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.*;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import org.bukkit.Bukkit;

import java.io.IOException;

public class SWMUtils {

    private static SlimePlugin getSlimePlugin() {
        return (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
    }

    private static SlimePropertyMap getDefaultProperties() {
        SlimePropertyMap properties = new SlimePropertyMap();

        properties.setString(SlimeProperties.DIFFICULTY, "normal");
        properties.setInt(SlimeProperties.SPAWN_X, 0);
        properties.setInt(SlimeProperties.SPAWN_Y, 100);
        properties.setInt(SlimeProperties.SPAWN_Z, 0);

        return properties;
    }

    public static void loadWorld(String worldName) throws UnknownWorldException, IOException, CorruptedWorldException, NewerFormatException, WorldInUseException {
        SlimeLoader loader = getSlimePlugin().getLoader("file");

        // Note that this method should be called asynchronously
        SlimeWorld world = getSlimePlugin().loadWorld(loader, worldName, false, getDefaultProperties());

        // This method must be called synchronously
        getSlimePlugin().generateWorld(world);
    }

    public static void createEmptyWorld(String worldName) throws IOException, WorldAlreadyExistsException {
        SlimeLoader loader = getSlimePlugin().getLoader("file");

        // Note that this method should be called asynchronously
        getSlimePlugin().createEmptyWorld(loader, worldName, false, getDefaultProperties());
    }
}
