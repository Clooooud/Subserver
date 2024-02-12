package com.stackmc.subserver.listeners;

import com.stackmc.subserver.SubServer;
import com.stackmc.subserver.instance.Instance;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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

        if(!(plugin.getConfig().getBoolean("settings.auto-join-instance"))) return;
        Instance instance = Instance.getInstance(event.getPlayer().getWorld());
        if (instance == null) return;
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
