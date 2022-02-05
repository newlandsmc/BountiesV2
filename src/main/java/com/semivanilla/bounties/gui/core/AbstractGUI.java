package com.semivanilla.bounties.gui.core;

import com.semivanilla.bounties.utils.modules.InternalPlaceholders;
import com.semivanilla.bounties.utils.modules.MessageFormatter;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public abstract class AbstractGUI {

    protected final GUIHandler handler;

    public AbstractGUI(GUIHandler handler) {
        this.handler = handler;
    }

    public void openMenu(@NotNull final Player player){

        prepareGUI(player).thenAccept((gui) -> {
            handler.getPlugin().getServer().getScheduler().runTask(handler.getPlugin(), new Runnable() {
                public void run() {
                    gui.open(player);
                }
            });
        });
    }
    protected abstract CompletableFuture<BaseGui> prepareGUI(final Player player);

    public abstract String name();
}
