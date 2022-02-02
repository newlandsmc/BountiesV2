package com.semivanilla.bounties.api;

import com.semivanilla.bounties.model.Bounty;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface BountiesAPI {

    boolean isPlayerBounty(@NotNull final Player player);

    @NotNull
    Optional<Bounty> getBountyFor(@NotNull final Player player);

    @NotNull
    Optional<UUID> getLastKilledPlayer(@NotNull final Player player);

    int getPossibleLevelForBounty(@NotNull final Player player);

    @NotNull
    String storageType();

}
