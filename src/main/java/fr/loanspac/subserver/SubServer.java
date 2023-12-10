package fr.loanspac.subserver;

import fr.loanspac.subserver.commands.SubServerCommand;
import fr.loanspac.subserver.listeners.WorldInitListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class SubServer extends JavaPlugin {

    private List<Listener> listeners = new ArrayList<>();

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("===========================");
        Bukkit.getLogger().info("Enable SubServer 1.0");
        Bukkit.getLogger().info("===========================");

        this.listeners.add(new WorldInitListener());
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
