package com.stackmc.subserver;

import com.stackmc.subserver.commands.SubServerCommand;
import com.stackmc.subserver.instance.Instance;
import com.stackmc.subserver.listeners.InstanceListener;
import com.stackmc.subserver.listeners.WorldInitListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class SubServer extends JavaPlugin {
    private List<Listener> listeners = new ArrayList<>();
    @Getter private List<Instance> instances = new ArrayList<>();

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("===========================");
        Bukkit.getLogger().info("Enable SubServer 1.0");
        Bukkit.getLogger().info("===========================");

        this.listeners.add(new WorldInitListener());
        this.listeners.add(new InstanceListener(this));
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("===========================");
        Bukkit.getLogger().info("Disable SubServer 1.0");
        Bukkit.getLogger().info("===========================");
    }

    public void registerCommands(){
        this.getCommand("subserver").setExecutor(new SubServerCommand(this));
    }

    public void registerListeners(){
        this.listeners.forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }
}