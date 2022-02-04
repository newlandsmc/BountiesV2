package com.semivanilla.bounties.gui.core;

import me.mattstudios.mfgui.gui.guis.BaseGui;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public abstract class AbstractGUI {

    protected final GUIHandler handler;

    public AbstractGUI(GUIHandler handler) {
        this.handler = handler;
    }

    public void openMenu(@NotNull final Player player){
        prepareGUI(player).thenAccept(gui -> {
           handler.getPlugin().getServer().getScheduler().runTask(handler.getPlugin(), new Runnable() {
               @Override
               public void run() {
                    gui.open(player);
               }
           });
        });
    }

    protected abstract CompletableFuture<BaseGui> prepareGUI(final Player player);

    public abstract String name();
}
