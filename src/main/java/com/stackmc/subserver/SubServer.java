package com.stackmc.subserver;

import com.stackmc.subserver.commands.SubServerCommand;
import com.stackmc.subserver.instance.Instance;
import com.stackmc.subserver.listeners.EventListener;
import com.stackmc.subserver.listeners.InstanceListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class SubServer extends JavaPlugin {

    private final List<Listener> listeners = new ArrayList<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.listeners.add(new InstanceListener(this));
        this.listeners.add(new EventListener(this));
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        Instance.getInstances().forEach(Instance::close);
        Instance.getInstances().clear();
    }

    public void registerCommands(){
        this.getCommand("subserver").setExecutor(new SubServerCommand(this));
    }

    public void registerListeners(){
        this.listeners.forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }
}