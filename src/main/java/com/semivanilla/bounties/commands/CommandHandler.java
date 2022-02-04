package com.semivanilla.bounties.commands;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.commands.command.BountyAdminCommand;
import com.semivanilla.bounties.commands.command.BountyCommand;
import me.mattstudios.mf.base.CommandManager;
import me.mattstudios.mf.base.components.CompletionResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandHandler {

    private final Bounties plugin;
    private final CommandManager manager;

    public static final HashMap<String, String> playerHelpMap = new HashMap<>(){{
        put("/bounty","Opens up the GUI for bounty");
        put("/bounty help","Show help for the plugin");
    }};

    public static final HashMap<String,String> adminHelpMap = new HashMap<>(){{
        put("/bountyadmin create [player]","Creates a bounty on the specified player with the default time limit");
        put("/bountyadmin create [player] [mins]","Creates a bounty on the specified player with the specified time limit");
        put("/bountyadmin remove [player]","Removes the bounty of a player if the player is a bounty.");
        put("/bountyadmin bypass [player]","Adds/Removes the player on the player exempt list. Once added, they won't be bounty-ed nor will release a bounty until the server restart");
        put("/bountyadmin bypass [player] true","Adds the player on to the player exempt list");
        put("/bountyadmin bypass [player] false","Removes the player on to the player exempt list");
        put("/bountyadmin set [player] [kills]","Sets the amount of kills a bounty-ed player have");
        put("/bountyadmin stats [player] bkill [kill]","Set the stats of no of bounties the player killed with the specified value");
        put("/bountyadmin stats [player] kill [kill]","Set the stats of no of non-bounties the player killed with the specified value");
        put("/bountyadmin stats [player] death [death]","Set the stats of no of player-caused death of the player with the specified value");
        put("/bountyadmin stats [player] reset","Reset all the stats of the player to 0");
        put("/bountyadmin reload","Reloads the plugin");
    }};

    private static final List<String> booleanTabCompleter = new ArrayList<>(){{
        add("true");
        add("false");
    }};

    private static final List<String> statTabCompleter = new ArrayList<>(){{
       add("bkill");
       add("kill");
       add("death");
       add("reset");
    }};

    public CommandHandler(Bounties plugin) {
        this.plugin = plugin;
        this.manager = new CommandManager(plugin);
    }

    public void registerOthers(){
        manager.getCompletionHandler().register("#onlinebounty", new CompletionResolver() {
            @Override
            public List<String> resolve(Object input) {
                return plugin.getDataManager().getOnlineBountyNames();
            }
        });

        manager.getCompletionHandler().register("#boolean", new CompletionResolver() {
            @Override
            public List<String> resolve(Object input) {
                return booleanTabCompleter;
            }
        });

        manager.getCompletionHandler().register("#stat", new CompletionResolver() {
            @Override
            public List<String> resolve(Object input) {
                return statTabCompleter;
            }
        });
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
