package com.semivanilla.bounties.gui;

import com.semivanilla.bounties.gui.core.AbstractGUI;
import com.semivanilla.bounties.gui.core.GUIHandler;
import me.mattstudios.mfgui.gui.guis.BaseGui;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class BountyMenu extends AbstractGUI {

    public BountyMenu(GUIHandler handler) {
        super(handler);
    }

    @Override
    protected CompletableFuture<BaseGui> prepareGUI(Player player) {
        return null;
    }

    @Override
    public String name() {
        return null;
    }
}
