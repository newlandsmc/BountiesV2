package com.semivanilla.bounties.commands.command;

import com.semivanilla.bounties.commands.CommandHandler;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("bounty")
public class BountyCommand extends CommandBase {

    private final CommandHandler handler;

    public BountyCommand(CommandHandler handler) {
        this.handler = handler;
    }

    @Default
    public void onDefaultCommand(final Player player){
        handler.getPlugin().getGuiHandler().getBountyMenu().openMenu(player);
    }

    @SubCommand("help")
    public void onHelpCommand(final CommandSender sender){
        handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender,handler.getPlugin().getConfiguration().getMessageHelpHeader());
        CommandHandler.playerHelpMap.forEach((command,descr) -> {
            handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender,handler.getPlugin().getConfiguration().getMessageHelpContent(command,descr));
        });
        if(sender.hasPermission("bounty.admin")){
            CommandHandler.adminHelpMap.forEach((command,descr) -> {
                handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender,handler.getPlugin().getConfiguration().getMessageHelpContent(command,descr));
            });
        }
        handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender,handler.getPlugin().getConfiguration().getMessageHelpFooter());
    }


}
