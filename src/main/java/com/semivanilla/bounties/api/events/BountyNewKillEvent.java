package com.semivanilla.bounties.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BountyNewKillEvent extends Event {

    private final UUID playerUID;
    private final int updatedKill;

    public BountyNewKillEvent(UUID playerUID, int updatedKill) {
        this.playerUID = playerUID;
        this.updatedKill = updatedKill;
    }

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public UUID getPlayerUID() {
        return playerUID;
    }

    public int getUpdatedKill() {
        return updatedKill;
    }
}
