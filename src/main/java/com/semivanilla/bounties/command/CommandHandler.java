package com.semivanilla.bounties.command;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.command.command.BountyAdminCommand;
import com.semivanilla.bounties.command.command.BountyCommand;
import me.mattstudios.mf.base.CommandManager;

public class CommandHandler {

    private final Bounties plugin;
    private final CommandManager manager;

    public CommandHandler(Bounties plugin) {
        this.plugin = plugin;
        this.manager = new CommandManager(plugin);
    }

    public void registerCommands(){
        manager.register(
                new BountyCommand(this),
                new BountyAdminCommand(this)
        );
    }

    public Bounties getPlugin() {
        return plugin;
    }
}
