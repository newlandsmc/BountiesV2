package com.semivanilla.bounties.commands.command;

import com.semivanilla.bounties.commands.CommandHandler;
import com.semivanilla.bounties.enums.DefaultResponse;
import com.semivanilla.bounties.model.PlayerStatistics;
import com.semivanilla.bounties.utils.modules.InternalPlaceholders;
import com.semivanilla.bounties.utils.modules.MessageFormatter;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

@Command("bountyadmin")
@Alias({"boadmin","bountiesadmin"})
public class BountyAdminCommand extends CommandBase {

    private final CommandHandler handler;

    public BountyAdminCommand(CommandHandler handler) {
        this.handler = handler;
    }

    public void onDefaultCommand(final CommandSender sender){
        sender.sendMessage("Help");
    }

    @SubCommand("create")
    @Permission("bounty.command.create")
    @Completion({"#players","#range"})
    public void onCreateCommand(final CommandSender sender, final Player player, @Optional Integer time){
        if(player == null){
            handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender, DefaultResponse.INVALID_PLAYER_ARGS.getResponse());
            return;
        }

        if(handler.getPlugin().getDataManager().isPlayerBounty(player)){
            handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender, DefaultResponse.ALREADY_HAS_BOUNTY.getResponse());
            return;
        }

        if(handler.getPlugin().getDataManager().isPlayerExempted(player.getUniqueId()) || player.hasPermission("bounty.bypass")){
            handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender, DefaultResponse.PLAYER_IS_EXEMPTED_FROM_BOUNTY.getResponse());
            return;
        }

        if(time == null || time < 1){
            handler.getPlugin().getDataManager().getBountyManager().createBountyOn(player.getUniqueId());
        }else {
            final long shouldLastUpto= System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(time);
            handler.getPlugin().getDataManager().getBountyManager().createBountyOn(player.getUniqueId(),shouldLastUpto);
        }
        handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender, DefaultResponse.SUCCESSFULLY_CREATED_BOUNTY.getResponse());
    }

    @SubCommand("remove")
    @Permission("bounty.command.remove")
    @Completion({"#onlinebounty"})
    public void onRemoveCommand(final CommandSender sender, final Player player){
        if(player == null){
            handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender, DefaultResponse.INVALID_PLAYER_ARGS.getResponse());
            return;
        }

        if(!handler.getPlugin().getDataManager().isPlayerBounty(player)){
            handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender, DefaultResponse.IS_NOT_A_BOUNTY.getResponse());
            return;
        }

        handler.getPlugin().getDataManager().getBountyManager().clearBountyOn(player.getUniqueId());
        handler.getPlugin().getDataManager().getPlayerTrackerManager().removeTrackerOn(player.getUniqueId());
        handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender, DefaultResponse.SUCCESSFULLY_REMOVED_BOUNTY.getResponse());
    }

    @SubCommand("bypass")
    @Permission("bounty.command.bypass")
    @Completion({"#players","#boolean"})
    public void onCommandBypass(final CommandSender sender, final Player player, Boolean status){
        if(player == null){
            handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender, DefaultResponse.INVALID_PLAYER_ARGS.getResponse());
            return;
        }

        if(handler.getPlugin().getDataManager().isPlayerBounty(player)){
            handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender, DefaultResponse.CANNOT_EXEMPT_ON_ACTIVE_BOUNTY .getResponse());
            return;
        }

        if(status){
            if(handler.getPlugin().getDataManager().isPlayerExempted(player.getUniqueId())){
                handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender, DefaultResponse.PLAYER_ALREADY_EXEMPTED.getResponse());
                return;
            }
            handler.getPlugin().getDataManager().addToExemptedList(player);
            handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender, DefaultResponse.SUCCESSFULLY_EXEMPTED_FROM_BOUNTY.getResponse());
        }else {
            if(!handler.getPlugin().getDataManager().isPlayerExempted(player.getUniqueId())){
                handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender, DefaultResponse.PLAYER_ALREADY_NOT_EXEMPTED.getResponse());
                return;
            }
            handler.getPlugin().getDataManager().removeFromExemptedList(player);
            handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender, DefaultResponse.SUCCESSFULLY_REMOVED_FROM_EXEMPT.getResponse());
        }
    }

    @SubCommand("reload")
    @Permission("bounty.command.reload")
    public void onReloadCommand(final CommandSender sender){
        handler.getPlugin().reloadPlugin();
        handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender,DefaultResponse.PLUGIN_RELOADED.getResponse());
    }

    @SubCommand("setkills")
    @Permission("bounty.command.set")
    @Completion({"#players","#range"})
    public void onCommandSet(final CommandSender sender, final Player player, final Integer kills){
        if(kills == null || kills < 1){
            handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender,DefaultResponse.INVALID_ARGS.getResponse());
            return;
        }

        if(player == null){
            handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender, DefaultResponse.INVALID_PLAYER_ARGS.getResponse());
            return;
        }

        if(!handler.getPlugin().getDataManager().isPlayerBounty(player)){
            handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender, DefaultResponse.IS_NOT_A_BOUNTY.getResponse());
            return;
        }

        handler.getPlugin().getDataManager().getBountyManager().getBounty(player).setKilled(kills);
        handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender, DefaultResponse.SUCCESSFULLY_SET_KILLS.getResponse());
    }

    @SubCommand("setstats")
    @Permission("bounty.command.stats")
    @Completion({"#players","#stat","#range"})
    public void onCommandStats(final CommandSender sender, final Player player, String stats, @Optional Integer value){
        if(stats == null){
            handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender,DefaultResponse.INVALID_ARGS.getResponse());
            return;
        }

        if(player == null){
            handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender, DefaultResponse.INVALID_PLAYER_ARGS.getResponse());
            return;
        }

        switch (stats.toUpperCase()){
            case "BKILL":
            case "BOUNTYKILL":
                if(value == null || value < 0){
                    handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender,DefaultResponse.INVALID_ARGS.getResponse());
                    return;
                }

                if(handler.getPlugin().getDataManager().getStatisticsManager().getStatisticsHashMap().containsKey(player.getUniqueId())){
                    handler.getPlugin().getDataManager().getStatisticsManager().getStatisticsHashMap().get(player.getUniqueId()).setBountyKills(value);
                    handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender,DefaultResponse.SET_STATS_OF_BKILLS.getResponse());
                }else{
                    handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender,DefaultResponse.UNKNOWN_PLAYER_ON_STATS.getResponse());
                    return;
                }
                break;

            case "KILL":
                if(value == null || value < 0){
                    handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender,DefaultResponse.INVALID_ARGS.getResponse());
                    return;
                }

                if(handler.getPlugin().getDataManager().getStatisticsManager().getStatisticsHashMap().containsKey(player.getUniqueId())){
                    handler.getPlugin().getDataManager().getStatisticsManager().getStatisticsHashMap().get(player.getUniqueId()).setKills(value);
                    handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender,DefaultResponse.SET_STATS_OF_KILLS.getResponse());
                }else{
                    handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender,DefaultResponse.UNKNOWN_PLAYER_ON_STATS.getResponse());
                    return;
                }
                break;

            case "DEATH":
                if(value == null || value < 0){
                    handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender,DefaultResponse.INVALID_ARGS.getResponse());
                    return;
                }

                if(handler.getPlugin().getDataManager().getStatisticsManager().getStatisticsHashMap().containsKey(player.getUniqueId())){
                    handler.getPlugin().getDataManager().getStatisticsManager().getStatisticsHashMap().get(player.getUniqueId()).setDeaths(value);
                    handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender,DefaultResponse.SET_STATS_OF_DEATH.getResponse());
                }else{
                    handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender,DefaultResponse.UNKNOWN_PLAYER_ON_STATS.getResponse());
                    return;
                }
                break;
            case "RESET":
                if(handler.getPlugin().getDataManager().getStatisticsManager().getStatisticsHashMap().containsKey(player.getUniqueId())){
                    handler.getPlugin().getDataManager().getStatisticsManager().getStatisticsHashMap().get(player.getUniqueId()).setDeaths(0);
                    handler.getPlugin().getDataManager().getStatisticsManager().getStatisticsHashMap().get(player.getUniqueId()).setKills(0);
                    handler.getPlugin().getDataManager().getStatisticsManager().getStatisticsHashMap().get(player.getUniqueId()).setBountyKills(0);
                    handler.getPlugin().getDatabaseHandler().getDataStorage().savePlayerStatisticsAsync(handler.getPlugin().getDataManager().getStatisticsManager().getStatisticsHashMap().get(player.getUniqueId()));
                    handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender,DefaultResponse.STATS_RESET.getResponse());
                }else{
                    handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender,DefaultResponse.UNKNOWN_PLAYER_ON_STATS.getResponse());
                    return;
                }
                break;
            default:
        }
    }

    @SubCommand("cleartracker")
    @Permission("bounty.command.cleartracker")
    @Completion({"#players"})
    public void onClearCommand(@NotNull final CommandSender sender, final Player player){
        if(player == null){
            handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender, DefaultResponse.INVALID_PLAYER_ARGS.getResponse());
            return;
        }

        if(!handler.getPlugin().getDataManager().getPlayerTrackerManager().getKilledTrackerMap().containsKey(player.getUniqueId())){
            handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender,DefaultResponse.PLAYER_NOT_A_TRACKER.getResponse());
            return;
        }

        handler.getPlugin().getDataManager().getPlayerTrackerManager().removeTrackerOn(player.getUniqueId());
        handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender,DefaultResponse.REMOVED_TRACKER_ON.getResponse());
    }

    @SubCommand("stat")
    @Permission("bounty.command.stat")
    @Completion({"#players"})
    public void onCommandStat(@NotNull final CommandSender sender, final Player player){
        if(player == null){
            handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender, DefaultResponse.INVALID_PLAYER_ARGS.getResponse());
            return;
        }

        PlayerStatistics statistics = handler.getPlugin().getDataManager().getStatisticsManager().getPlayerStatistics(player);
        if(statistics == null){
            handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender,DefaultResponse.UNABLE_TO_FETCH_STAT.getResponse());
            return;
        }

        handler.getPlugin().getUtilityManager().getMessagingUtils().sendTo(sender,
            handler.getPlugin().getConfiguration().getMessagePlayerStats(
                    new InternalPlaceholders("%kd%",statistics.getKDRatio()),
                    new InternalPlaceholders("%deaths%",statistics.getDeaths()),
                    new InternalPlaceholders("%bkills%",statistics.getBountyKills()),
                    new InternalPlaceholders("%non_bkills%",statistics.getKills()),
                    new InternalPlaceholders("%name%",player.getName())
            )
        );
    }


}
