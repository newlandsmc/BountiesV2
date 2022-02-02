package com.semivanilla.bounties.model;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public final class PlayerTracker {

    private final UUID killer;
    private final UUID deadPlayer;
    private long killedAt;

    public PlayerTracker(UUID killer, UUID deadPlayer) {
        this.killer = killer;
        this.deadPlayer = deadPlayer;
        this.killedAt = System.currentTimeMillis();
    }

    public void setKilledAt(long killedAt) {
        this.killedAt = killedAt;
    }

    public void updateKillAgain(){
        this.killedAt = System.currentTimeMillis();
    }

    public long getKilledAt() {
        return killedAt;
    }

    public UUID getKiller() {
        return killer;
    }

    public UUID getDeadPlayer() {
        return deadPlayer;
    }

    public boolean isKillerKilledSameAgain(@NotNull UUID killer, @NotNull UUID deadPlayer){
        return this.killer.equals(killer) && this.deadPlayer.equals(deadPlayer);
    }

    public Optional<Player> getKillerPlayerRef(){
        return Optional.ofNullable(Bukkit.getPlayer(killer));
    }

    public Optional<Player> getDeadPlayerRef(){
        return Optional.ofNullable(Bukkit.getPlayer(deadPlayer));
    }
}
