package com.stackmc.subserver.instance;

import com.stackmc.subserver.SubServer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class Instance {

    private static final List<Instance> instances = new ArrayList<>();

    public static Instance getInstance(World world) {
        return instances.stream().filter(instance -> instance.worlds.contains(world)).findAny().orElse(null);
    }

    public final String name;
    public final SubServer plugin;
    public final List<World> worlds = new ArrayList<>();
    public final Set<OfflinePlayer> offlinePlayers = new HashSet<>();

    public void register() {
        instances.add(this);
    }

    public void addWorld(World world) {
        worlds.add(world);
    }

    public void joinInstance(Player player) {
        getPlayers().forEach(target -> {
            player.showPlayer(plugin, target);
            target.showPlayer(plugin, player);
        });
        offlinePlayers.add(player);
        player.teleport(worlds.get(0).getSpawnLocation());
    }

    public void onChatEvent(Player player, String message) {
        sendMessageToInstance("<" + player.getDisplayName() + "> " + message);
    }

    public void sendMessageToInstance(String message) {
        getPlayers().forEach(receiver -> receiver.sendMessage(message));
    }

    public List<Player> getPlayers() {
        return offlinePlayers.stream().filter(OfflinePlayer::isOnline).map(OfflinePlayer::getPlayer).collect(Collectors.toList());
    }
}