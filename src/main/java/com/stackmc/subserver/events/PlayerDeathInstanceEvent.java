package com.stackmc.subserver.events;

import com.stackmc.subserver.instance.Instance;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class PlayerDeathInstanceEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Setter private boolean cancelled;
    @Setter private String deathMessage;
    private final Player player;
    private final Instance instance;

    public PlayerDeathInstanceEvent(String deathMessageEvent, Player playerEvent, Instance instanceEvent) {
        deathMessage = deathMessageEvent;
        player = playerEvent;
        instance = instanceEvent;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

