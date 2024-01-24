package com.stackmc.subserver.instance;

import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.stackmc.subserver.SubServer;
import com.stackmc.subserver.worldgen.SWMUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class Instance {

    @Getter private static final Set<Instance> instances = new HashSet<>();

    public static Instance getInstance(World world) {
        return instances.stream().filter(instance -> instance.getWorlds().contains(world)).findAny().orElse(null);
    }

    public static Instance getInstance(String name) {
        return instances.stream().filter(instance -> instance.getName().contains(name)).findAny().orElse(null);
    }

    private final String name;
    private final SubServer plugin;
    private final List<World> worlds = new ArrayList<>();
    private final Set<OfflinePlayer> offlinePlayers = new HashSet<>();
    private final UUID uniqueId = UUID.randomUUID();
    @Setter private String chatMessage;
    @Setter private String joinMessage;
    @Setter private String quitMessage;
    @Setter private String deathMessage;

    public void register() {
        instances.add(this);
    }

    public void close() {
        offlinePlayers.forEach(offlinePlayer -> {
            Player player = offlinePlayer.getPlayer();
            if (player != null) {
                player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            }
        });

        worlds.forEach(world -> {
            Bukkit.unloadWorld(world, false);
            try {
                SWMUtils.deleteWorld(world.getName());
            } catch (UnknownWorldException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        worlds.clear();
        offlinePlayers.clear();
    }

    public void loadWorld(String worldName, @Nullable Consumer<String> callback) {
        final Consumer<String> finalCallback = (callback == null ? (s -> {}) : callback);

        String destWorldName = getUniqueId().toString() + "_" + worldName;

        File src = new File(SWMUtils.getWorldSlimeFolder() + File.separator + worldName + ".slime");
        File dest = new File( SWMUtils.getWorldSlimeFolder() + File.separator + destWorldName + ".slime");

        long startTime = System.currentTimeMillis();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                Files.copy(src.toPath(), dest.toPath());
            } catch (IOException e) {
                finalCallback.accept("§cLe monde spécifié (" + worldName + ") n'existe pas.");
                return;
            }

            Bukkit.getScheduler().runTask(plugin, () -> {
                try {
                    SWMUtils.loadWorld(destWorldName);
                } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException |
                         WorldInUseException e) {
                    finalCallback.accept("§cUne erreur est survenue lors du chargement du monde.");
                }
            });

            World world;
            do {
                world = Bukkit.getWorld(destWorldName);
            } while (world == null); // I can do this because i'm in an async thread

            this.addWorld(world);

            long totalTime = System.currentTimeMillis() - startTime;
            finalCallback.accept("Monde " + destWorldName +  " chargé en " + totalTime + "ms ou " + ((float) totalTime / 50f) + " ticks .");
        });
    }

    public void addWorld(World world) {
        worlds.add(world);
    }

    public void joinInstance(Player player) {
        Instance oldInstance = Instance.getInstance(player.getWorld());
        if(oldInstance != null) oldInstance.quitInstance(player);
        getPlayers().forEach(target -> {
            player.showPlayer(plugin, target);
            target.showPlayer(plugin, player);
        });
        offlinePlayers.add(player);
        player.teleport(worlds.get(0).getSpawnLocation());
    }

    public void quitInstance(Player player) {
        onQuitEvent(player);
        getPlayers().forEach(target -> {
            player.hidePlayer(plugin, target);
            target.hidePlayer(plugin, player);
        });
        offlinePlayers.remove(player);
    }

    public void onChatEvent(Player player, String message) {
        if(getChatMessage() == null) {
            setChatMessage(plugin.getConfig().getString("instance-event.chat"));
        }
        sendMessageToInstance(getChatMessage().replace("player", player.getDisplayName()).replace("message", message));
    }

    public void onJoinEvent(Player player) {
        if(getJoinMessage() == null) {
            setJoinMessage(plugin.getConfig().getString("instance-event.join"));
        }
        sendMessageToInstance(getJoinMessage().replace("player", player.getDisplayName()));
    }

    public void onQuitEvent(Player player) {
        if(getQuitMessage() == null) {
            setQuitMessage(plugin.getConfig().getString("instance-event.quit"));
        }
        sendMessageToInstance(getQuitMessage().replace("player", player.getDisplayName()));
    }

    public void onDeathEvent(Player player) {
        if(getDeathMessage() == null) {
            setDeathMessage(plugin.getConfig().getString("instance-event.death"));
        }
        sendMessageToInstance(getDeathMessage().replace("player", player.getDisplayName()));
    }

    public void sendMessageToInstance(String message) {
        getPlayers().forEach(receiver -> receiver.sendMessage(message));
    }

    public List<Player> getPlayers() {
        return offlinePlayers.stream().filter(OfflinePlayer::isOnline).map(OfflinePlayer::getPlayer).collect(Collectors.toList());
    }
}