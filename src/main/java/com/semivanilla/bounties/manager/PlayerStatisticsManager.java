package com.semivanilla.bounties.manager;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.model.PlayerStatistics;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public final class PlayerStatisticsManager {

    private final Bounties plugin;
    private final HashMap<UUID, PlayerStatistics> statisticsHashMap;

    public PlayerStatisticsManager(Bounties plugin) {
        this.plugin = plugin;
        this.statisticsHashMap = new HashMap<>();
    }

    public void insertPlayerStatistics(@NotNull PlayerStatistics statistics){
        this.statisticsHashMap.put(statistics.getPlayerID(),statistics);
    }

    public void unloadPlayerStatistics(@NotNull UUID uuid){
        this.statisticsHashMap.remove(uuid);
    }

    public PlayerStatistics getPlayerStatistics(@NotNull UUID uid){
        return statisticsHashMap.get(uid);
    }

    public PlayerStatistics getPlayerStatistics(@NotNull Player player){
        return statisticsHashMap.get(player.getUniqueId());
    }

    public Iterator<PlayerStatistics> getAllLoadedPlayerStats(){
        return statisticsHashMap.values().stream().iterator();
    }

    public void addDeathForPlayer(@NotNull UUID uid){
        if(!statisticsHashMap.containsKey(uid))
            return;

        statisticsHashMap.get(uid).addDeath();
    }

    public void addBountyKillForPlayer(@NotNull UUID uid){
        if(!statisticsHashMap.containsKey(uid))
            return;

        statisticsHashMap.get(uid).addBountyKills();
    }

    public void addKillForPlayer(@NotNull UUID uid){
        if(!statisticsHashMap.containsKey(uid))
            return;

        statisticsHashMap.get(uid).addKill();
    }

    public HashMap<UUID, PlayerStatistics> getStatisticsHashMap() {
        return statisticsHashMap;
    }
}
