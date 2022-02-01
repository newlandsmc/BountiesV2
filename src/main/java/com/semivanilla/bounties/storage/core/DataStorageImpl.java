package com.semivanilla.bounties.storage.core;

import com.semivanilla.bounties.model.Bounty;
import com.semivanilla.bounties.storage.H2;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Optional;
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
     * see {@link H2#prepareDatabaseTables()} for more
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
     * This method does not hold any run-time value. It's just to notify the user what is the database mode
     * @return String of what the storage type will be
     */
    String storageType();

    /**
     * Gets an Optional Future if the Bounties' data is present in the Database
     *
     * This method was used before favouring {@link DataStorageImpl#getAllCurrentBounties()} use case.
     * see {@link DataStorageImpl#getAllCurrentBounties()}
     * @param uuid UUID of the player to be fetched
     * @return A Future or Optionally Wrapped Bounty Object
     */
    CompletableFuture<Optional<Bounty>> getIfPresent(@NotNull UUID uuid);

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
     * if the data is in memory it will be easy to handle data's when situations like Combat-Logging happens
     * @return Iterator of all the bounties
     */
    Iterator<Bounty> getAllCurrentBounties();

}
