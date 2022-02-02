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
     * 1 -> Check if there are any reward queued upon the player
     * 2 -> If there are reward queued up on the player, execute it and remove those reward from database
     */
    @EventHandler
    public void onPlayerConnect(PlayerJoinEvent event){
        final Player player = event.getPlayer();

        plugin.getDataManager().getRewardQueueManager().getAllQueueIfPresent(player.getUniqueId()).ifPresent((queuedRewardList) -> {
            if(queuedRewardList.isEmpty())
                return;

            plugin.getLogger().info("Seems like there are queued rewards for player "+player.getName());

            for(BountyQueue queue : queuedRewardList){
                plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        if(queue.getAction() == QueueAction.ADD_XP){
                            plugin.getHookManager().getXPImpl().addXPForPlayer(player,queue.getValueToExecute());
                        }else {
                            plugin.getHookManager().getXPImpl().removeXPForPlayer(player, queue.getValueToExecute());
                        }

                        plugin.getDataManager().getRewardQueueManager().removeQueue(queue);
                    }
                },5);
            }
        });
    }

    /**
     * Tasks this event should undertake
     * 1 ->  Remove them from the list as the exempted Player only persist data of
     * a single session
     */
    @EventHandler
    public void onPlayerDisconnectEvent(PlayerQuitEvent event){
        final Player player = event.getPlayer();

        plugin.getDataManager().removeFromExemptedList(player);
    }

}
