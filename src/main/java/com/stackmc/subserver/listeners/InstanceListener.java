package com.stackmc.subserver.listeners;

import com.stackmc.subserver.SubServer;
import com.stackmc.subserver.instance.Instance;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class InstanceListener implements Listener {
    private final SubServer plugin;

    public InstanceListener(SubServer plugin) {
        this.plugin = plugin;
    }

    private boolean inInstance(World world) {
        for (Instance instance : plugin.getInstances()) {
            if (instance.getWorlds().contains(world)) {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(!(inInstance(event.getPlayer().getWorld()))) {
            event.setJoinMessage(null);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if(!(inInstance(event.getPlayer().getWorld()))) {
            event.setCancelled(true);
        }
    }
}
