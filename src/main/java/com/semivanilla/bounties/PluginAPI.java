package com.semivanilla.bounties;

import com.semivanilla.bounties.api.BountiesAPI;
import com.semivanilla.bounties.model.Bounty;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public final class PluginAPI implements BountiesAPI {

    private final Bounties plugin;

    public PluginAPI(Bounties plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isPlayerBounty(@NotNull final Player player) {
        return plugin.getDataManager().isPlayerBounty(player);
    }

    @Override
    public @NotNull Optional<Bounty> getBountyFor(@NotNull final Player player) {
        return Optional.ofNullable(plugin.getDataManager().getBountyManager().getBountiesHashMap().getOrDefault(player.getUniqueId(), null));
    }

    @Override
    public @NotNull Optional<UUID> getLastKilledPlayer(@NotNull final Player player) {
        return Optional.empty();
    }

    @Override
    public int getPossibleLevelForBounty(@NotNull final Player player) {
        if(!plugin.getDataManager().isPlayerBounty(player))
            return 0;

        return plugin.getConfiguration().getXPForKills(plugin.getDataManager().getBountyManager().getBountiesHashMap().get(player.getUniqueId()).getKilled());
    }

    @Override
    public @NotNull String storageType() {
        return plugin.getDatabaseHandler().getDataStorage().storageType();
    }
}
