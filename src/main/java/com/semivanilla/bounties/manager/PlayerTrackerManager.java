package com.semivanilla.bounties.manager;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.model.PlayerTracker;
import com.semivanilla.bounties.task.PlayerTrackerTask;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public final class PlayerTrackerManager {

    private final Bounties plugin;
    private final HashMap<UUID, PlayerTracker> killedTrackerMap;
    private final PlayerTrackerTask playerTrackerTask;

    public PlayerTrackerManager(Bounties plugin) {
        this.plugin = plugin;
        this.killedTrackerMap = new HashMap<>();
        this.playerTrackerTask = new PlayerTrackerTask(this);
        this.playerTrackerTask.runTaskTimerAsynchronously(plugin,60,20);
    }

    public void addPlayerTracker(@NotNull PlayerTracker tracker){
        killedTrackerMap.put(tracker.getKiller(),tracker);
    }

    public void removeTrackerOn(@NotNull UUID uuid){
        killedTrackerMap.remove(uuid);
    }

    public boolean isDuplicatedKill(@NotNull UUID killer, @NotNull UUID deadPlayer){
        if(!killedTrackerMap.containsKey(killer))
            return false;

        return killedTrackerMap.get(killer).isKillerKilledSameAgain(killer,deadPlayer);
    }

    public void updateKillForDuplicatedKill(@NotNull UUID killer){
        if(!killedTrackerMap.containsKey(killer))
            return;

        killedTrackerMap.get(killer).updateKillAgain();
    }

    public HashMap<UUID, PlayerTracker> getKilledTrackerMap() {
        return killedTrackerMap;
    }

    public Bounties getPlugin() {
        return plugin;
    }

    public PlayerTrackerTask getPlayerTrackerTask() {
        return playerTrackerTask;
    }
}
