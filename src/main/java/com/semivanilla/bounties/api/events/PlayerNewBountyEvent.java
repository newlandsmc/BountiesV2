package com.semivanilla.bounties.api.events;

import com.semivanilla.bounties.model.Bounty;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class PlayerNewBountyEvent extends Event{

    private final Bounty bounty;

    public PlayerNewBountyEvent(Bounty bounty) {
        this.bounty = bounty;
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

    public Bounty getBounty() {
        return bounty;
    }
}
