package com.semivanilla.bounties;

import com.semivanilla.bounties.manager.BountyManager;
import com.semivanilla.bounties.manager.PlayerTrackerManager;
import com.semivanilla.bounties.manager.RewardQueueManager;
import com.semivanilla.bounties.model.Bounty;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


public final class PluginDataManager {

    private final Bounties plugin;
    private final BountyManager bountyManager;
    private final PlayerTrackerManager playerTrackerManager;
    private final RewardQueueManager rewardQueueManager;

    private final List<UUID> exemptedPlayerList;

    public PluginDataManager(Bounties plugin) {
        this.plugin = plugin;
        this.bountyManager = new BountyManager(plugin);
        this.playerTrackerManager = new PlayerTrackerManager(plugin);
        this.rewardQueueManager = new RewardQueueManager(plugin);
        this.exemptedPlayerList = new ArrayList<>();
    }

    public boolean isPlayerBounty(@NotNull Player player){
        return bountyManager.isBounty(player.getUniqueId());
    }

    public boolean isPlayerBounty(@NotNull UUID uuid){
        return bountyManager.isBounty(uuid);
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

    public void addToExemptedList(@NotNull final Player player){
        exemptedPlayerList.add(player.getUniqueId());
    }

    public void removeFromExemptedList(@NotNull final Player player){
        exemptedPlayerList.remove(player.getUniqueId());
    }

    public boolean isPlayerExempted(@NotNull final UUID uidPlayer){
        return exemptedPlayerList.contains(uidPlayer);
    }


}
