package fr.loanspac.subserver.managers;

import fr.loanspac.subserver.SubServer;
import fr.loanspac.subserver.listeners.WorldInitListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class ListenerManager {
    SubServer main = SubServer.instance();

    private List<Listener> listeners = new ArrayList<>();

    public void registerEvents(){
        this.listeners.add(new WorldInitListener());
        this.listeners.forEach((listener -> {
            Bukkit.getPluginManager().registerEvents(listener, main);
        }));
    }
}
