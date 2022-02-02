package com.semivanilla.bounties.model;

import com.semivanilla.bounties.enums.QueueAction;

import java.util.UUID;

public final class BountyQueue {

    private final UUID uuid;
    private final QueueAction action;
    private final int valueToExecute;

    public BountyQueue(UUID uuid, QueueAction action, int valueToExecute) {
        this.uuid = uuid;
        this.action = action;
        this.valueToExecute = valueToExecute;
    }

    public UUID getUuid() {
        return uuid;
    }

    public QueueAction getAction() {
        return action;
    }

    public int getValueToExecute() {
        return valueToExecute;
    }
}
