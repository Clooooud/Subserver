package fr.loanspac.subserver.managers;

import fr.loanspac.subserver.SubServer;
import fr.loanspac.subserver.commands.staff.GenerateAsyncWorld;

import java.util.Objects;

public class CommandManager {
    SubServer main = SubServer.instance();

    public void setCommand(){
        Objects.requireNonNull(main.getCommand("aworld")).setExecutor(new GenerateAsyncWorld());
        Objects.requireNonNull(main.getCommand("worldtp")).setExecutor(new GenerateAsyncWorld());
    }
}
