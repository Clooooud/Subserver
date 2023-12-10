package fr.loanspac.subserver;

import fr.loanspac.subserver.managers.CommandManager;
import fr.loanspac.subserver.managers.ListenerManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SubServer extends JavaPlugin {
    private static SubServer INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        Bukkit.getLogger().info("===========================");
        Bukkit.getLogger().info("Enable SubServer 1.0");
        Bukkit.getLogger().info("===========================");

        ListenerManager listeners = new ListenerManager();
        listeners.registerEvents();

        CommandManager commands = new CommandManager();
        commands.setCommand();
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("===========================");
        Bukkit.getLogger().info("Disable SubServer 1.0");
        Bukkit.getLogger().info("===========================");
    }

    public static SubServer instance() {
        return INSTANCE;
    }
}
