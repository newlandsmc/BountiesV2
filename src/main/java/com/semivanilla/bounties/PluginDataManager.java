package com.semivanilla.bounties;

import com.semivanilla.bounties.hook.HookManager;
import com.semivanilla.bounties.manager.BountyManager;
import com.semivanilla.bounties.manager.PlayerTrackerManager;
import com.semivanilla.bounties.manager.RewardQueueManager;
import com.semivanilla.bounties.model.Bounty;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;


public class PluginDataManager {

    private final Bounties plugin;
    private final BountyManager bountyManager;
    private final PlayerTrackerManager playerTrackerManager;
    private final RewardQueueManager rewardQueueManager;

    public PluginDataManager(Bounties plugin) {
        this.plugin = plugin;
        this.bountyManager = new BountyManager(plugin);
        this.playerTrackerManager = new PlayerTrackerManager(plugin);
        this.rewardQueueManager = new RewardQueueManager(plugin);
    }

    public boolean isPlayerBounty(@NotNull Player player){
        return bountyManager.isBounty(player.getUniqueId());
    }

    public void loadAllBounties(){
        plugin.getDatabaseHandler().getDataStorage().getAllCurrentBounties().forEachRemaining(bountyManager::loadBounty);
        plugin.getLogger().info("Found/Loaded details of "+bountyManager.getCurrentBountySize()+" bounties!");
        plugin.getDatabaseHandler().getDataStorage().getAllBountyQueues().forEachRemaining(rewardQueueManager::populateRewardQueue);
        plugin.getLogger().info("Found/Loaded details of "+rewardQueueManager.getActiveQueueListSize()+" queued rewards!");
    }

    public BountyManager getBountyManager() {
        return bountyManager;
    }

    public PlayerTrackerManager getPlayerTrackerManager() {
        return playerTrackerManager;
    }

    public RewardQueueManager getRewardQueueManager() {
        return rewardQueueManager;
    }

    public Iterator<Bounty> getAllBounties(){
        return bountyManager.getBountiesHashMap().values().iterator();
    }

    public Iterator<Bounty> getOnlineBounties(){
        return bountyManager.getBountiesHashMap().values().stream().filter(Bounty::isPlayerOnline).iterator();
    }


}
