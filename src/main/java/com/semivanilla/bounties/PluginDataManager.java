package com.semivanilla.bounties;

import com.semivanilla.bounties.manager.BountyManager;
import com.semivanilla.bounties.manager.PlayerTrackerManager;


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
}
