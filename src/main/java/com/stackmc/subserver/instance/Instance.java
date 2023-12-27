package com.stackmc.subserver.instance;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.World;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class Instance {
    public final String name;
    public final List<World> worlds;
}