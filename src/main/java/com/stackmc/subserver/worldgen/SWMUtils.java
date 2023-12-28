package com.stackmc.subserver.worldgen;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.*;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

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

    public static String worldSlimeFolder() {
        try {
            File src = new File("plugins/SlimeWorldManager/sources.yml");
            Scanner myReader = new Scanner(src);
            myReader.nextLine();

            String data = myReader.nextLine();
            String[] dataSplit = data.split(":");
            String dataPart = dataSplit[1];
            String[] folderNameSplit = dataPart.split(" ");
            String folderName = folderNameSplit[1];
            myReader.close();
            return folderName;
        } catch (FileNotFoundException e) {
            Bukkit.getLogger().severe("§cLa source n'existe pas.");
            return null;
        }
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
