package com.semivanilla.bounties.task;

import com.semivanilla.bounties.manager.PlayerTrackerManager;
import com.semivanilla.bounties.model.PlayerTracker;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class PlayerTrackerTask extends BukkitRunnable {

    private final PlayerTrackerManager manager;

    public PlayerTrackerTask(PlayerTrackerManager manager) {
        this.manager = manager;
    }

    @Override
    public void run() {
        if(manager.getKilledTrackerMap().isEmpty())
            return;

        final long durationInMillis = TimeUnit.SECONDS.toMillis( manager.getPlugin().getConfiguration().getPlayerKillTagDuration());
        Iterator<Map.Entry<UUID, PlayerTracker>> entryIterator = manager.getKilledTrackerMap().entrySet().iterator();

        while (entryIterator.hasNext()){
            Map.Entry<UUID,PlayerTracker> mapEntry = entryIterator.next();
             final long shouldExpireOn = mapEntry.getValue().getKilledAt() + durationInMillis;
             if(System.currentTimeMillis() >= shouldExpireOn){
                 entryIterator.remove();
             }
        }
    }
}
