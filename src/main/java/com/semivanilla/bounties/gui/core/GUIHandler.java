package com.semivanilla.bounties.gui.core;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.gui.BountyMenu;

public class GUIHandler {

    private final Bounties plugin;
    private final BountyMenu bountyMenu;

    public GUIHandler(Bounties plugin) {
        this.plugin = plugin;
        this.bountyMenu = new BountyMenu(this);
    }

    public Bounties getPlugin() {
        return plugin;
    }

    public BountyMenu getBountyMenu() {
        return bountyMenu;
    }
}
