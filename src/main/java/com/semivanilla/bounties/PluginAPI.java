package com.semivanilla.bounties;

import com.semivanilla.bounties.api.BountiesAPI;
import com.semivanilla.bounties.model.Bounty;
import com.semivanilla.bounties.model.PlayerStatistics;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class PluginAPI implements BountiesAPI {

    private final Bounties plugin;

    public PluginAPI(Bounties plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isPlayerBounty(@NotNull final OfflinePlayer player) {
        return plugin.getDataManager().isPlayerBounty(player.getUniqueId());
    }

    @Override
    public boolean isPlayerOnlineBounty(@NotNull Player player) {
        return plugin.getDataManager().isPlayerBounty(player);
    }

    @Override
    public @NotNull Optional<Bounty> getBountyFor(@NotNull final Player player) {
        return Optional.ofNullable(plugin.getDataManager().getBountyManager().getBountiesHashMap().getOrDefault(player.getUniqueId(), null));
    }

    @Override
    public @NotNull Optional<UUID> getLastKilledPlayer(@NotNull final Player player) {
        if(plugin.getDataManager().getPlayerTrackerManager().getKilledTrackerMap().containsKey(player.getUniqueId())){
            return Optional.of(plugin.getDataManager().getPlayerTrackerManager().getKilledTrackerMap().get(player.getUniqueId()).getDeadPlayer());
        }else return Optional.empty();
    }

    @Override
    public @NotNull Optional<PlayerStatistics> getPlayerKDStatistics(@NotNull Player player) {
        return Optional.ofNullable(plugin.getDataManager().getStatisticsManager().getStatisticsHashMap().getOrDefault(player.getUniqueId(),null));
    }

    @Override
    public @NotNull CompletableFuture<PlayerStatistics> getOfflinePlayerStatistics(@NotNull UUID uuid) {
        if(plugin.getDataManager().getStatisticsManager().getStatisticsHashMap().containsKey(uuid)) {
            CompletableFuture<PlayerStatistics> playerStatisticsCompletableFuture = new CompletableFuture<>();
            playerStatisticsCompletableFuture.complete(plugin.getDataManager().getStatisticsManager().getPlayerStatistics(uuid));
            return playerStatisticsCompletableFuture;
        }
        return plugin.getDatabaseHandler().getDataStorage().getOrRegister(uuid);
    }

    @Override
    public int getXPWorthFor(@NotNull final Player player) {
        if(!plugin.getDataManager().isPlayerBounty(player))
            return 0;

        return plugin.getConfiguration().getXPForKills(plugin.getDataManager().getBountyManager().getBountiesHashMap().get(player.getUniqueId()).getKilled());
    }

    @Override
    public @NotNull String storageType() {
        return plugin.getDatabaseHandler().getDataStorage().storageType();
    }
}
