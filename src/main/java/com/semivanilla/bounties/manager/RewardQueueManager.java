package com.semivanilla.bounties.manager;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.enums.QueueAction;
import com.semivanilla.bounties.model.BountyQueue;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public final class RewardQueueManager {

    private final Bounties plugin;
    private final List<BountyQueue> queueList;

    public RewardQueueManager(Bounties plugin) {
        this.plugin = plugin;
        this.queueList = new ArrayList<>();
    }

    public void populateRewardQueue(@NotNull BountyQueue queue){
        this.queueList.add(queue);
    }

    public Optional<List<BountyQueue>> getAllQueueIfPresent(@NotNull UUID uuid){
        return Optional.of(queueList.stream().filter(queue->queue.getUuid().equals(uuid)).collect(Collectors.toList()));
    }

    public int getActiveQueueListSize(){
        return queueList.size();
    }

    public void removeQueue(@NotNull BountyQueue queue){
        this.queueList.remove(queue);
        plugin.getDatabaseHandler().getDataStorage().removeBountyQueue(queue.getUuid());
    }

    public void registerNewRewardQueue(@NotNull UUID player, @NotNull QueueAction action, int value){
        final BountyQueue queue = new BountyQueue(player,action,value);
        this.populateRewardQueue(queue);
        this.plugin.getDatabaseHandler().getDataStorage().registerBountyQueue(queue);
    }
}
