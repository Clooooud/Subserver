package com.stackmc.subserver.worldgen;

import com.infernalsuite.aswm.api.SlimePlugin;
import com.infernalsuite.aswm.api.exceptions.*;
import com.infernalsuite.aswm.api.loaders.SlimeLoader;
import com.infernalsuite.aswm.api.world.SlimeWorld;
import com.infernalsuite.aswm.api.world.properties.SlimeProperties;
import com.infernalsuite.aswm.api.world.properties.SlimePropertyMap;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class SWMUtils {
    private static String slimeFolder = null;
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

    @NotNull
    public static String getWorldSlimeFolder() {
        if (slimeFolder != null) {
            return slimeFolder;
        }

        try {
            File src = new File("plugins/SlimeWorldManager/sources.yml");
            Scanner myReader = new Scanner(src);
            String read = "file:";
            while(!(myReader.nextLine().equals(read))) {
                myReader.nextLine();
            }
            String data = myReader.nextLine();
            String[] dataSplit = data.split(":");
            String dataPart = dataSplit[1];
            String[] folderNameSplit = dataPart.split(" ");
            slimeFolder = folderNameSplit[1];
            myReader.close();
            return slimeFolder;
        } catch (FileNotFoundException e) {
            Bukkit.getLogger().severe("§cLa source n'existe pas.");
            return "";
        }
    }

    public static void deleteWorld(String worldName) {
        SlimeLoader loader = getSlimePlugin().getLoader("file");
        try {
            loader.deleteWorld(worldName);
        }catch (UnknownWorldException | IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static void loadWorld(String worldName) throws UnknownWorldException, IOException, CorruptedWorldException, NewerFormatException, WorldLoadedException, WorldLockedException {
        SlimeLoader loader = getSlimePlugin().getLoader("file");

        // note that this method should be called asynchronously
        SlimeWorld world = getSlimePlugin().loadWorld(loader, worldName, false, getDefaultProperties());

        // note that this method must be called synchronously
        getSlimePlugin().loadWorld(world);
    }
}
