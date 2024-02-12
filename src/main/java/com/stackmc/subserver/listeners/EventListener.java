package com.stackmc.subserver.listeners;

import com.stackmc.subserver.SubServer;
import com.stackmc.subserver.instance.Instance;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.hanging.HangingEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.vehicle.VehicleEvent;
import org.bukkit.event.weather.WeatherEvent;
import org.bukkit.event.world.WorldEvent;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class EventListener implements Listener {

    private final SubServer plugin;

    public EventListener(SubServer plugin) {
        this.plugin = plugin;

        catchAllEvents();
    }

    private void catchAllEvents() {
        // The magic happens here
        Reflections reflections = new Reflections("org.bukkit.event."); // Scan all classes in the org.bukkit.event package
        Set<Class<? extends Event>> eventClasses = reflections.getSubTypesOf(Event.class); // Get all classes that extend Event

        for (Class<? extends Event> eventClass : eventClasses) { // For each event class
            if (Modifier.isAbstract(eventClass.getModifiers())) { // If the class is abstract, skip it
                continue;
            }
            // Register the event
            Bukkit.getPluginManager().registerEvent(eventClass, this, EventPriority.MONITOR, (listener, event) -> onEvent(event), this.plugin);
        }
    }

    public void onEvent(Event event) {
        Instance instance = fetchInstance(event);
        if (instance == null) {
            return;
        }

        instance.dispatchEvent(event);
    }

    private Instance fetchInstance(Event event) {
        World world = fetchWorld(event);
        if (world == null) {
            return null;
        }

        return Instance.getInstance(world);
    }

    private final Map<Class<? extends Event>, Function<Event, World>> worldFetchers = new HashMap<>();

    {
        worldFetchers.put(HangingEvent.class, event -> ((HangingEvent) event).getEntity().getWorld());
        worldFetchers.put(InventoryEvent.class, event -> ((InventoryEvent) event).getView().getPlayer().getWorld());
        worldFetchers.put(EntityEvent.class, event -> ((EntityEvent) event).getEntity().getWorld());
        worldFetchers.put(PlayerEvent.class, event -> ((PlayerEvent) event).getPlayer().getWorld());
        worldFetchers.put(BlockEvent.class, event -> ((BlockEvent) event).getBlock().getWorld());
        worldFetchers.put(VehicleEvent.class, event -> ((VehicleEvent) event).getVehicle().getWorld());
        worldFetchers.put(WeatherEvent.class, event -> ((WeatherEvent) event).getWorld());
        worldFetchers.put(WorldEvent.class, event -> ((WorldEvent) event).getWorld());
    }

    private World fetchWorld(Event event) {
        return worldFetchers.entrySet().stream()
                .filter(entry -> entry.getKey().isInstance(event))
                .findAny()
                .map(entry -> entry.getValue().apply(event))
                .orElse(null);
    }


}
