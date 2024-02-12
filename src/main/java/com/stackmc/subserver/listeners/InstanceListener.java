package com.stackmc.subserver.listeners;

import com.stackmc.subserver.SubServer;
import com.stackmc.subserver.instance.Instance;
import com.stackmc.subserver.instance.InstanceType;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.stream.Collectors;

public class InstanceListener implements Listener {

    private final SubServer plugin;

    public InstanceListener(SubServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getOnlinePlayers().forEach(target -> {
            event.getPlayer().hidePlayer(plugin, target);
            target.hidePlayer(plugin, event.getPlayer());
        });
        event.setJoinMessage(null);

        List<InstanceType> autospawnTypes = this.plugin.getInstanceFactory().getInstanceTypes().stream()
                .filter(InstanceType::isAutoJoin)
                .collect(Collectors.toList());

        if (autospawnTypes.isEmpty()) {
            System.err.println("No instance type is set to auto-join.");
            return;
        }

        if (autospawnTypes.size() > 1) {
            System.err.println("More than one instance type is set to auto-join.");
            return;
        }

        InstanceType type = autospawnTypes.get(0);
        Instance instance = this.plugin.getInstanceFactory().getInstances(type).stream()
                .filter(inst -> inst.getPlayers().size() < type.getMaxPlayers())
                .findAny()
                .orElse(null);

        if (instance == null) {
            System.err.println("No instance are open for auto-join.");
            event.getPlayer().kickPlayer("Server is full, sorry.");
            return;
        }

        instance.joinInstance(event.getPlayer());
    }

    /*@EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        Instance instance = Instance.getInstance(event.getPlayer().getWorld());
        if (instance == null) return;
        instance.onQuitEvent(event.getPlayer());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);

        Instance instance = Instance.getInstance(event.getEntity().getPlayer().getWorld());
        if (instance == null) return;
        instance.onDeathEvent(event.getEntity().getPlayer());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        Instance instance = Instance.getInstance(event.getPlayer().getWorld());

        if (instance == null) {
            Bukkit.getLogger().severe("UN JOUEUR TENTE DE PARLER DANS UN MONDE HORS INSTANCE: " + event.getMessage());
            return;
        }

        instance.onChatEvent(event.getPlayer(), event.getMessage());
    }*/
}
