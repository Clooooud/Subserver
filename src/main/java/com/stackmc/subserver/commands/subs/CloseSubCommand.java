package com.stackmc.subserver.commands.subs;

import com.stackmc.subserver.SubServer;
import com.stackmc.subserver.instance.Instance;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class CloseSubCommand implements TabExecutor {

    private final SubServer plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command rootCommand, String label, String[] args) {
        if(args.length == 0) {
            sender.sendMessage("§cVous devez préciser un nom d'instance.");
            return false;
        }
        Instance instance = Instance.getInstance(args[0]);
        instance.close();
        Instance.getInstances().remove(instance);
        sender.sendMessage(String.format("L'instance %s est maintenant fermée.", instance.getName()));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command rootCommand, String label, String[] args) {
        return Collections.emptyList();
    }
}