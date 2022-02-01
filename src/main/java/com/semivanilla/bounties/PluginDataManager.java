package com.semivanilla.bounties;

import com.semivanilla.bounties.manager.BountyManager;
import com.semivanilla.bounties.manager.PlayerTrackerManager;
import com.semivanilla.bounties.model.Bounty;

import java.util.Iterator;


public class PluginDataManager {

    private final Bounties plugin;
    private final BountyManager bountyManager;
    private final PlayerTrackerManager playerTrackerManager;

    public PluginDataManager(Bounties plugin) {
        this.plugin = plugin;
        this.bountyManager = new BountyManager(plugin);
        this.playerTrackerManager = new PlayerTrackerManager(plugin);
    }

    public void loadAllBounties(){
        plugin.getDatabaseHandler().getDataStorage().getAllCurrentBounties().forEachRemaining(bountyManager::loadBounty);
        plugin.getLogger().info("Found/Loaded details of "+bountyManager.getCurrentBountySize()+" bounties!");
    }

    public BountyManager getBountyManager() {
        return bountyManager;
    }

    public PlayerTrackerManager getPlayerTrackerManager() {
        return playerTrackerManager;
    }

    public Iterator<Bounty> getAllBounties(){
        return bountyManager.getBountiesHashMap().values().iterator();
    }

    public Iterator<Bounty> getOnlineBounties(){
        return bountyManager.getBountiesHashMap().values().stream().filter(Bounty::isPlayerOnline).iterator();
    }
}
