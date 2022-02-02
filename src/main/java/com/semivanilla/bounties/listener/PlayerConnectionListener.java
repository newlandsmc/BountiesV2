package com.semivanilla.bounties.listener;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.enums.QueueAction;
import com.semivanilla.bounties.model.BountyQueue;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerConnectionListener implements Listener {

    private final Bounties plugin;

    public PlayerConnectionListener(Bounties plugin) {
        this.plugin = plugin;
    }

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

}
