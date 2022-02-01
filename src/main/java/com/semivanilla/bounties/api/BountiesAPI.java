package com.semivanilla.bounties.api;

import com.semivanilla.bounties.model.Bounty;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface BountiesAPI {

    boolean isPlayerBounty(@NotNull Player player);

    Optional<Bounty> getBountyFor(@NotNull Player player);

    Optional<UUID> getLastKilledPlayer(@NotNull Player player);

    int getPossibleLevelForBounty(@NotNull Player player);

    String storageType();

}
