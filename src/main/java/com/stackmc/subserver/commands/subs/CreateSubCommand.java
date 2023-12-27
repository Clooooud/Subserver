package com.stackmc.subserver.commands.subs;

import com.grinderwolf.swm.api.exceptions.WorldAlreadyExistsException;
import com.stackmc.subserver.SubServer;
import com.stackmc.subserver.worldgen.SWMUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class CreateSubCommand implements TabExecutor {

    private final SubServer plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command rootCommand, String label, String[] args) {
        if(args[0].isEmpty()) {
            sender.sendMessage("§cVous devez préciser un nom de monde.");
            return false;
        }

        String worldName = args[0];
        long startTime = System.currentTimeMillis();

        try {
            SWMUtils.createEmptyWorld(worldName);
        } catch (WorldAlreadyExistsException | IOException ex) {
            sender.sendMessage("§cUne erreur est survenue lors de la création du monde.");
            return false;
        }

        long totalTime = System.currentTimeMillis() - startTime;
        sender.sendMessage("Monde créé en " + totalTime + "ms ou " + ((float) totalTime / 50f) + " ticks .");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command rootCommand, String label, String[] args) {
        return Collections.emptyList();
    }
}