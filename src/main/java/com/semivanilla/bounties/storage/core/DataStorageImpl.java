package com.semivanilla.bounties.storage.core;

import com.semivanilla.bounties.model.Bounty;
import com.semivanilla.bounties.model.BountyQueue;
import com.semivanilla.bounties.model.PlayerStatistics;
import com.semivanilla.bounties.storage.SQLite;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface DataStorageImpl {

    /**
     * Configure and try a connection to Data-Storage Needed
     * @return Boolean Whether the connection was successful
     */
    boolean initStorageConnection();

    /**
     * If using something like SQL, you could use this method for preparing/creating tables and all. Can be empty if its
     * something like JSON and all!
     *
     * see {@link SQLite#prepareDatabaseTables()} for more
     */
    void prepareDatabaseTables();

    /**
     * Closes the connection to the database.
     * This is called when the server is getting shutdown.
     *
     * NOTE: Don't call it during run-time. It's not recommended closing the connection after each query
     */
    void close();

    /**
     * Returns a String of the database used.
     * For eg: MySQL, MongoDB etc
     *
     * You should also do data-saving on here. Before closing the connection if it's an SQL
     *
     * This method does not hold any run-time value. It's just to notify the user what is the database mode
     * @return String of what the storage type will be
     */
    String storageType();

    /**
     * This is a method used to fetch a player statistics ({@link PlayerStatistics} data from the database.
     * The method is defined with in the name as getOrRegister, and as the name suggest, it should either get the data
     * if it exists in database or if the requested data is not present, one should register a new data to the database
     * and return the newly created along with it.
     *
     * In no case this should return a null value, if an exception is occurred, one can pass the
     * {@link PlayerStatistics#PlayerStatistics(UUID)} constructor which will construct the object with the
     * default values
     *
     * NOTE: PlayerStatistics are not stored in memory after a player leaves. That means it's loaded and updated when the
     * player joins the server, leaves and server shutdowns.
     *
     * @param uuid UUID of the player that needed to be fetched/registered
     * @return A future of PlayerStatistics
     */
    CompletableFuture<PlayerStatistics> getOrRegister(UUID uuid);

    /**
     * Adds a new bounty to the storage when created!
     * @param bounty A Bounty object that's created when the bounty has been made
     */
    void registerNewBounty(@NotNull Bounty bounty);

    /**
     * Remove a user from database
     * @param uuid UUID of the player to be removed
     */
    void removeABounty(@NotNull UUID uuid);

    /**
     * Returns an iterator of all available bounties on the database.
     * NOTE: The reason this method is not async it because it SHOULD ONLY be called on server startup.
     * This is used for populating the memory with all active bounties.
     *
     * I took this method over loading individual bounty loading, is because considering the fact that this is a custom-made
     * plugin for Semi-Vanilla MC and not a public plugin, and the use case of the plugin will be with less than 200
     * players as of now. Even with 1000 player populating them shouldn't cause much of issue. Another primary reason is
     * that with the data in memory it will be easy to handle data's when situations like Combat-Logging happens
     * @return Iterator of all the bounties
     */
    Iterator<Bounty> getAllCurrentBounties();

    /**
     * Saves the details of a bounty to the database
     *
     * This should do an async task, This method will be called during the run-time and saving them in the main thread
     * may cause the serious performance issues
     * @param bounty
     */
    void saveBountyAsync(@NotNull Bounty bounty);

    /**
     * Saves the detail of a bounty to the database
     *
     * This will be a synchronized task and will be used on the {@link JavaPlugin#onDisable()} or specifically {@link DataStorageImpl#close()} method. Since onDisable()
     * method can't register an async task, This method will be only used to save the data to the database
     * on server shutdown
     * @param bounty An of all the bounties the server has
     */
    void saveBountySync(@NotNull Bounty bounty);

    /**
     * Save the statistics of a player on the main thread.
     *
     * This will be a synchronized task and will be used on the {@link JavaPlugin#onDisable()} or specifically {@link DataStorageImpl#close()} method. Since onDisable()
     * method can't register an async task, This method will be only used to save the data to the database
     * on server shutdown
     * @param statistics Statistics of the player
     */
    void savePlayerStatisticsSync(@NotNull PlayerStatistics statistics);

    /**
     * Save the statistics of a player on the main thread.
     *
     * This should do an async task, This method will be called during the run-time and saving them in the main thread
     * may cause the serious performance issues
     * @param statistics
     */
    void savePlayerStatisticsAsync(@NotNull PlayerStatistics statistics);
    /**
     * Register a new reward queue to the database, if the player combat-logged in situations
     * @param queue {@link BountyQueue} object.
     */
    void registerBountyQueue(@NotNull BountyQueue queue);

    /**
     * Removes a queue of {@link BountyQueue} from the database
     * @param uuid UID of the player to be removed
     */
    void removeBountyQueue(@NotNull UUID uuid);

    /**
     * Returns an Iterator for all the available Bounty Queue's in the database
     * NOTE: The reason this method is not async it because it SHOULD ONLY be called on server startup.
     * This is used for populating the memory with all active bounty queue.
     *
     * This needed to be loaded only once and there shouldn't be a method needed to update/save to the database later.
     * As once the reward is executed, the plugin will call {@link DataStorageImpl#removeBountyQueue(UUID)}.
     * @return
     */
    Iterator<BountyQueue> getAllBountyQueues();
}
