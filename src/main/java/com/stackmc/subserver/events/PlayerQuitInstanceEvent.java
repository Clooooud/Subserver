package com.stackmc.subserver.events;

import com.stackmc.subserver.instance.Instance;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class PlayerQuitInstanceEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Setter private boolean cancelled;
    @Setter private String joinMessage;
    private final Player player;
    private final Instance instance;

    public PlayerQuitInstanceEvent(String joinMessageEvent, Player playerEvent, Instance instanceEvent) {
        joinMessage = joinMessageEvent;
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
