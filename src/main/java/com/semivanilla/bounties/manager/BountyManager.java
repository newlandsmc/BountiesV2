package com.semivanilla.bounties.manager;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.model.Bounty;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BountyManager {

    private final Bounties plugin;
    private final HashMap<UUID, Bounty> bountiesHashMap;

    public BountyManager(Bounties plugin) {
        this.plugin = plugin;
        this.bountiesHashMap = new HashMap<UUID, Bounty>();
    }

    public void loadBounty(@NotNull UUID uuid){
        plugin.getDatabaseHandler().getDataStorage().getIfPresent(uuid).thenAccept((bounty -> {
            if(bounty.isEmpty())
                return;
            bountiesHashMap.put(bounty.get().getPlayerUUID(),bounty.get());
        }));
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
    }

    public void clearBountyOn(@NotNull UUID killer){
        final Bounty bounty = bountiesHashMap.get(killer);
        if(!bounty.isPlayerOnline()){
            //TODO Add to remove XP Queue
        }

        bountiesHashMap.remove(bounty);
        plugin.getDatabaseHandler().getDataStorage().removeABounty(killer);
    }

    public void updateKillOn(@NotNull UUID killer){
        if(!bountiesHashMap.containsKey(killer))
            return;

        final Bounty bounty = bountiesHashMap.get(killer);
        bounty.addKill(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(plugin.getConfiguration().getBountyDuration()));
        //TODO Add Messages
    }

    public int getCurrentBountySize(){
        return bountiesHashMap.size();
    }


}
