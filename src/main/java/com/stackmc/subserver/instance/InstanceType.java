package com.stackmc.subserver.instance;

import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

@Getter
public class InstanceType {

    private final String name; // The name of the instance type, must be unique!!! (e.g. "plugin_lobby", "plugin_game")
    private final boolean autoJoin; // Whether players will join this instance automatically on server join, can be set only once by server
    private final int maxPlayers; // The maximum amount of players that can join this instance, -1 for unlimited
    @Setter private int maxInstancesCount = 2; // The maximum amount of OPEN instances of this type that can run at the same time, max 10

    private String[] worlds = new String[]{}; // The name of the worlds that are part of this instance

    @Setter
    private Consumer<Instance> postInitRunnable = (instance -> {}); // Executed after the instance has been initialized

    public InstanceType(String name, boolean autoJoin, int maxPlayers) {
        this.name = name;
        this.autoJoin = autoJoin;
        this.maxPlayers = maxPlayers;
    }

    public void setWorlds(String... worlds) {
        this.worlds = worlds;
    }

}
