package com.semivanilla.bounties.command.command;

import com.semivanilla.bounties.command.CommandHandler;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.entity.Player;

@Command("bounty")
public class BountyCommand extends CommandBase {

    private final CommandHandler handler;

    public BountyCommand(CommandHandler handler) {
        this.handler = handler;
    }

    @Default
    public void onDefaultCommand(final Player player){
        player.sendMessage("Open GUI");
    }


}
