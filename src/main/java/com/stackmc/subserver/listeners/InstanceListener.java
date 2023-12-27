package com.stackmc.subserver.listeners;

import com.stackmc.subserver.SubServer;
import com.stackmc.subserver.instance.Instance;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class InstanceListener implements Listener {

    private final SubServer plugin;

    public InstanceListener(SubServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        // TODO: afficher dans les instances un join
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Instance instance = Instance.getInstance(event.getPlayer().getWorld());

        if (instance == null) {
            return;
        }

        instance.onChatEvent(event.getPlayer(), event.getMessage());
        event.setCancelled(true);
    }
}
