package com.semivanilla.bounties.utils;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.utils.modules.MessageFormatter;
import com.semivanilla.bounties.utils.modules.MessagingUtils;

public final class UtilityManager {

    private final Bounties plugin;

    private final MessagingUtils messagingUtils;


    public UtilityManager(Bounties plugin) {
        this.plugin = plugin;
        this.messagingUtils = new MessagingUtils(this);
    }


    public Bounties getPlugin() {
        return plugin;
    }

    public MessagingUtils getMessagingUtils() {
        return messagingUtils;
    }
}
