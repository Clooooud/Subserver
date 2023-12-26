package com.stackmc.subserver.commands;


import com.stackmc.subserver.SubServer;
import com.stackmc.subserver.commands.subs.CreateSubCommand;
import com.stackmc.subserver.commands.subs.LoadSubCommand;
import com.stackmc.subserver.commands.subs.TpSubCommand;
import com.stackmc.subserver.commands.subs.UnloadSubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SubServerCommand extends AbstractCommand {

    public SubServerCommand(SubServer plugin) {
        super(plugin);
        this.registerSubCommand("create", new CreateSubCommand(plugin));
        this.registerSubCommand("tp", new TpSubCommand(plugin));
        this.registerSubCommand("load", new LoadSubCommand(plugin));
        this.registerSubCommand("unload", new UnloadSubCommand(plugin));
    }

    @Override
    public String getPermission() {
        return "subserver.admin";
    }

    @Override
    public boolean runCommand(CommandSender sender, Command rootCommand, String label, String[] args) {
        sender.sendMessage(this.getUsage());
        return true;
    }
}
