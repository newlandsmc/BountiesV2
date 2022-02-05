package com.semivanilla.bounties.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerBountyClearEvent extends Event {

    private final UUID uuid;

    public PlayerBountyClearEvent(UUID uuid) {
        this.uuid = uuid;
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

    public UUID getUuid() {
        return uuid;
    }
}
