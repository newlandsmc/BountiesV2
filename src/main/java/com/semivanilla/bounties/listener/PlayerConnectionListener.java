package com.semivanilla.bounties.listener;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.enums.QueueAction;
import com.semivanilla.bounties.model.BountyQueue;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerConnectionListener implements Listener {

    private final Bounties plugin;

    public PlayerConnectionListener(Bounties plugin) {
        this.plugin = plugin;
    }

    /**
     * Tasks the event must undertake
     * 1 -> Load their player statistics.
     * 2 -> Check if there are any reward queued upon the player.
     * 3 -> If there are reward queued up on the player, execute it and remove those reward from database.
     */
    @EventHandler
    public void onPlayerConnect(PlayerJoinEvent event){
        final Player player = event.getPlayer();

        plugin.getDatabaseHandler().getDataStorage().getOrRegister(player.getUniqueId()).thenAccept(stats -> {
            plugin.getDataManager().getStatisticsManager().insertPlayerStatistics(stats);
        });

        plugin.getDataManager().getRewardQueueManager().getAllQueueIfPresent(player.getUniqueId()).ifPresent((queuedRewardList) -> {
            if(queuedRewardList.isEmpty())
                return;

            plugin.getLogger().info("Seems like there are queued rewards for player "+player.getName());

            for(BountyQueue queue : queuedRewardList){
                plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        if(!player.isOnline()) {
                            plugin.getLogger().info("The player "+player.getName()+" went offline while executing rewards!");
                            return;
                        }

                        if(queue.getAction() == QueueAction.ADD_XP){
                            plugin.getHookManager().getXPImpl().addXPForPlayer(player,queue.getValueToExecute());
                        }else {
                            plugin.getHookManager().getXPImpl().removeXPForPlayer(player, queue.getValueToExecute());
                        }

                        plugin.getDataManager().getRewardQueueManager().removeQueue(queue);
                    }
                },20);
            }
        });
    }

    /**
     * Tasks this event should undertake
     * 1 ->  Remove them from the list as the exempted Player only persist data of
     * a single session
     *
     * 2 -> As a precaution save the data of the player when he logs out. A cache here won't be needed as the data is
     * not removed from memory
     */
    @EventHandler
    public void onPlayerDisconnectEvent(PlayerQuitEvent event){
        final Player player = event.getPlayer();

        plugin.getDataManager().removeFromExemptedList(player);

        if(plugin.getDataManager().isPlayerBounty(player)){
            plugin.getDatabaseHandler().getDataStorage().saveBountyAsync(plugin.getDataManager().getBountyManager().getBounty(player));
        }

        plugin.getDatabaseHandler().getDataStorage().savePlayerStatisticsAsync(plugin.getDataManager().getStatisticsManager().getPlayerStatistics(player));
        plugin.getDataManager().getStatisticsManager().unloadPlayerStatistics(player.getUniqueId());
    }

}
