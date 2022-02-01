package com.semivanilla.bounties.task;

import com.semivanilla.bounties.manager.BountyManager;
import com.semivanilla.bounties.model.Bounty;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class BountyExpiryTask extends BukkitRunnable {

    private final BountyManager manager;

    public BountyExpiryTask(BountyManager manager) {
        this.manager = manager;
    }

    @Override
    public void run() {
        final long currentTime = System.currentTimeMillis();
        if(manager.getBountiesHashMap().isEmpty())
            return;
        Iterator<Map.Entry<UUID, Bounty>> entryIterator = manager.getBountiesHashMap().entrySet().iterator();
        while (entryIterator.hasNext()){
            Map.Entry<UUID,Bounty> bountyEntry = entryIterator.next();
            if(System.currentTimeMillis() >= bountyEntry.getValue().getRemainingTime()) {
                manager.getPlugin().getDatabaseHandler().getDataStorage().removeABounty(bountyEntry.getValue().getPlayerUUID());
                entryIterator.remove();
            }
        }
    }
}
