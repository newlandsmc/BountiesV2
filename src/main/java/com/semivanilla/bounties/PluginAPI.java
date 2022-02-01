package com.semivanilla.bounties;

import com.semivanilla.bounties.api.BountiesAPI;
import com.semivanilla.bounties.model.Bounty;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class PluginAPI implements BountiesAPI {

    private final Bounties plugin;

    public PluginAPI(Bounties plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isPlayerBounty(@NotNull Player player) {
        return plugin.getDataManager().isPlayerBounty(player);
    }

    @Override
    public Optional<Bounty> getBountyFor(@NotNull Player player) {
        return Optional.ofNullable(plugin.getDataManager().getBountyManager().getBountiesHashMap().getOrDefault(player.getUniqueId(), null));
    }

    @Override
    public Optional<UUID> getLastKilledPlayer(@NotNull Player player) {
        return Optional.empty();
    }

    @Override
    public int getPossibleLevelForBounty(@NotNull Player player) {
        return 0;
    }

    @Override
    public String storageType() {
        return plugin.getDatabaseHandler().getDataStorage().storageType();
    }
}
