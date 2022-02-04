package com.semivanilla.bounties.api;

import com.semivanilla.bounties.model.Bounty;
import com.semivanilla.bounties.model.PlayerStatistics;
import com.semivanilla.bounties.storage.core.DataStorageImpl;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface BountiesAPI {

    /**
     * Checks whether the player <strong>(Both Online and Offline)</strong> is a bounty or not.
     * <p>
     * NOTE: This will return true, even if the player is offline as all the bounties are loaded onto the memory once the
     * plugin starts. Use {@link BountiesAPI#isPlayerOnlineBounty(Player)} if you want to check by if the
     * player is online.
     * </p>
     * @param player Player to be checked upon as bounty
     * @return boolean of whether the player is bounty or not
     */
    boolean isPlayerBounty(@NotNull final OfflinePlayer player);

    /**
     * Checks if an online player is a bounty or not.
     *
     * <p>NOTE: If you want to check whether an offline player is bounty or not use {@link BountiesAPI#isPlayerBounty(OfflinePlayer)}</p>
     * @param player Player to be checked upon as bounty
     * @return boolean of whether the player is bounty or not
     */
    boolean isPlayerOnlineBounty(@NotNull final Player player);

    /**
     * Gets the bounty object of a player with the provided player instance.
     *
     * <p>This will return an Optional element, so would be never null. If the player is not bounty, it will just return
     * {@link Optional#empty()}. So check whether the player is a bounty or use {@link Optional#isPresent()} before executing</p>
     * @param player Player of which the bounty object is requested
     * @return Optional of the bounty if present else an empty Optional
     * @see Bounty
     */
    @NotNull
    Optional<Bounty> getBountyFor(@NotNull final Player player);

    /**
     * Gets The UUID of the last player the requested player killed.
     *
     * <p>This will return an Optional element, so would be never null. If the player hasn't killed anybody yet or the
     * if the timer got over it would return an {@link Optional#empty()}. Else it would return the UUID of the dead player
     * </p>
     * @param player Player of which the last killer is requested
     * @return Optional UUID of the last player if present, else an empty Optional
     */
    @NotNull
    Optional<UUID> getLastKilledPlayer(@NotNull final Player player);

    /**
     * Gets the Statistics data of the player which the plugin tracks (Online Player).
     *
     * <p>NOTE: This would only return statistics of an <strong>Online Player</strong>. If you would like to have data of
     * Offline player use {@link BountiesAPI#getOfflinePlayerStatistics(UUID)}</p>
     *
     * <p>It would return an Optional PlayerStatistics if the player is online. Else would return an {@link Optional#empty()}</p>
     * @param player Player Of which the PlayerStatistics are requested
     * @return Optional PlayerStatistics if present, else an empty Optional
     * @see PlayerStatistics
     */
    @NotNull
    Optional<PlayerStatistics> getPlayerKDStatistics(@NotNull final Player player);

    /**
     * Gets the Statistics data of the player which the plugin tracks <strong>(Offline Player)</strong> from the database.
     *
     * <p>If the stats are present in the memory, it will load it from there, else would fetch from
     * {@link DataStorageImpl#getOrRegister(UUID)}</p>
     *
     * @param uuid UUID of the player which the PlayerStatistics are requested
     * @return A Future of PlayerStatistics from database.
     * @see PlayerStatistics
     */
    @NotNull
    CompletableFuture<PlayerStatistics> getOfflinePlayerStatistics(@NotNull final UUID uuid);

    /**
     * Would return the XP which is worth of killing the player if he is a <strong>bounty</strong> else would return 0;
     * @param player Bounty Player
     * @return XPWorth if the player is bounty, else 0
     */
    int getXPWorthFor(@NotNull final Player player);

    /**
     * Returns the storage method for the plugin
     * @return String of Storage Method Name
     * @see DataStorageImpl#storageType()
     */
    @NotNull
    String storageType();

}
