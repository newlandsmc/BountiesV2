package com.semivanilla.bounties.manager;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.api.events.BountyNewKillEvent;
import com.semivanilla.bounties.api.events.PlayerBountyClearEvent;
import com.semivanilla.bounties.api.events.PlayerNewBountyEvent;
import com.semivanilla.bounties.model.Bounty;
import com.semivanilla.bounties.task.BountyExpiryTask;
import com.semivanilla.bounties.utils.modules.InternalPlaceholders;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class BountyManager {

    private final Bounties plugin;
    private final HashMap<UUID, Bounty> bountiesHashMap;
    private final BountyExpiryTask expiryTask;

    public BountyManager(Bounties plugin) {
        this.plugin = plugin;
        this.bountiesHashMap = new HashMap<UUID, Bounty>();
        this.expiryTask = new BountyExpiryTask(this);
        this.expiryTask.runTaskTimerAsynchronously(plugin,60,20);
    }

    public void loadBounty(@NotNull Bounty bounty){
        bountiesHashMap.put(bounty.getPlayerUUID(),bounty);
    }

    public boolean isBounty(@NotNull UUID uuid){
        return bountiesHashMap.containsKey(uuid);
    }

    public void unloadBounty(@NotNull UUID uuid){
        bountiesHashMap.remove(uuid);
    }

    public void createBountyOn(@NotNull UUID killer){
        final long shouldLastUpto = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(plugin.getConfiguration().getBountyDuration());
        createBountyOn(killer,shouldLastUpto);
    }

    public void createBountyOn(@NotNull UUID killer, long shouldLastUpto){
        final Bounty bounty = new Bounty(killer,shouldLastUpto,1);
        bountiesHashMap.put(killer,bounty);
        plugin.getDatabaseHandler().getDataStorage().registerNewBounty(bounty);
        final PlayerNewBountyEvent event = new PlayerNewBountyEvent(bounty);
        plugin.getServer().getPluginManager().callEvent(event);
    }

    public void clearBountyOn(@NotNull UUID deadPlayer){
        final Bounty bounty = bountiesHashMap.get(deadPlayer);
        unloadBounty(deadPlayer);
        plugin.getDatabaseHandler().getDataStorage().removeABounty(deadPlayer);
        final PlayerBountyClearEvent event = new PlayerBountyClearEvent(deadPlayer);
        plugin.getServer().getPluginManager().callEvent(event);
    }

    public void updateKillOn(@NotNull UUID killer){
        if(!bountiesHashMap.containsKey(killer))
            return;

        final Bounty bounty = bountiesHashMap.get(killer);
        bounty.addKill(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(plugin.getConfiguration().getBountyDuration()));
        plugin.getDatabaseHandler().getDataStorage().saveBountyAsync(bounty);
        final BountyNewKillEvent event = new BountyNewKillEvent(killer,bounty.getKilled());
        plugin.getServer().getPluginManager().callEvent(event);
    }

    public int getCurrentBountySize(){
        return bountiesHashMap.size();
    }

    public HashMap<UUID, Bounty> getBountiesHashMap() {
        return bountiesHashMap;
    }

    public BountyExpiryTask getExpiryTask() {
        return expiryTask;
    }

    public Bounties getPlugin() {
        return plugin;
    }

    public Bounty getBounty(@NotNull UUID uuid){
        return bountiesHashMap.get(uuid);
    }

    public Bounty getBounty(@NotNull Player player){
        return this.getBounty(player.getUniqueId());
    }

}
