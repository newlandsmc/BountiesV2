package com.semivanilla.bounties.gui;

import com.semivanilla.bounties.gui.core.AbstractGUI;
import com.semivanilla.bounties.gui.core.GUIHandler;
import com.semivanilla.bounties.utils.modules.InternalPlaceholders;
import com.semivanilla.bounties.utils.modules.LocationUtils;
import com.semivanilla.bounties.utils.modules.MessageFormatter;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class BountyMenu extends AbstractGUI {

    public BountyMenu(GUIHandler handler) {
        super(handler);
    }

    @Override
    protected CompletableFuture<BaseGui> prepareGUI(Player player) {
        return CompletableFuture.supplyAsync(new Supplier<BaseGui>() {
            @Override
            public BaseGui get() {
                final PaginatedGui gui = Gui.paginated()
                        .disableAllInteractions()
                        .title(MessageFormatter.transform(handler.getPlugin().getConfiguration().getBountyMenuName()))
                        .rows(handler.getPlugin().getConfiguration().getBountyMenuRows()+1)
                        .create();

                handler.getPlugin().getConfiguration().getBountyMenuFillers().forEach(filler -> {
                    filler.getSlotList().forEach((slot) -> {
                        gui.setItem(slot, ItemBuilder.from(filler.getMaterial()).name(Component.empty()).asGuiItem());
                    });
                });

                if(handler.getPlugin().getConfiguration().isShowNextAndPreOnlyNeeded()){
                    if(handler.getPlugin().getDataManager().getOnlineBountyNames().size() > handler.getPlugin().getConfiguration().getBountyMenuRows()*9){
                        final GuiItem preButton = ItemBuilder.from(handler.getPlugin().getConfiguration().getBountyMenuPreviousButtons().getItemStack())
                                .name(MessageFormatter.transform(handler.getPlugin().getConfiguration().getBountyMenuPreviousButtons().getName()))
                                .lore(MessageFormatter.transform(handler.getPlugin().getConfiguration().getBountyMenuPreviousButtons().getLore()))
                                .amount(handler.getPlugin().getConfiguration().getBountyMenuPreviousButtons().getAmount())
                                .asGuiItem(event -> gui.previous());

                        final GuiItem nextButton = ItemBuilder.from(handler.getPlugin().getConfiguration().getBountyMenuNextButtons().getItemStack())
                                .name(MessageFormatter.transform(handler.getPlugin().getConfiguration().getBountyMenuNextButtons().getName()))
                                .lore(MessageFormatter.transform(handler.getPlugin().getConfiguration().getBountyMenuNextButtons().getLore()))
                                .amount(handler.getPlugin().getConfiguration().getBountyMenuNextButtons().getAmount())
                                .asGuiItem(event -> gui.next());

                        gui.setItem(handler.getPlugin().getConfiguration().getBountyMenuPreviousButtons().getSlot(),preButton);
                        gui.setItem(handler.getPlugin().getConfiguration().getBountyMenuNextButtons().getSlot(),nextButton);
                    }
                }else {
                    final GuiItem preButton = ItemBuilder.from(handler.getPlugin().getConfiguration().getBountyMenuPreviousButtons().getItemStack())
                            .name(MessageFormatter.transform(handler.getPlugin().getConfiguration().getBountyMenuPreviousButtons().getName()))
                            .lore(MessageFormatter.transform(handler.getPlugin().getConfiguration().getBountyMenuPreviousButtons().getLore()))
                            .amount(handler.getPlugin().getConfiguration().getBountyMenuPreviousButtons().getAmount())
                            .asGuiItem(event -> gui.previous());

                    final GuiItem nextButton = ItemBuilder.from(handler.getPlugin().getConfiguration().getBountyMenuNextButtons().getItemStack())
                            .name(MessageFormatter.transform(handler.getPlugin().getConfiguration().getBountyMenuNextButtons().getName()))
                            .lore(MessageFormatter.transform(handler.getPlugin().getConfiguration().getBountyMenuNextButtons().getLore()))
                            .amount(handler.getPlugin().getConfiguration().getBountyMenuNextButtons().getAmount())
                            .asGuiItem(event -> gui.next());

                    gui.setItem(handler.getPlugin().getConfiguration().getBountyMenuPreviousButtons().getSlot(),preButton);
                    gui.setItem(handler.getPlugin().getConfiguration().getBountyMenuNextButtons().getSlot(),nextButton);
                }

                final GuiItem statsItem = ItemBuilder.from(handler.getPlugin().getConfiguration().getBountyMenuStatsButton().getItemStack())
                        .name(MessageFormatter.transform(handler.getPlugin().getConfiguration().getBountyMenuStatsButton().getName()))
                        .amount(handler.getPlugin().getConfiguration().getBountyMenuStatsButton().getAmount())
                        .lore(MessageFormatter.transform( handler.getPlugin().getConfiguration().getBountyMenuStatsButton().getLore(
                                new InternalPlaceholders("%bkills%",handler.getPlugin().getDataManager().getStatisticsManager().getPlayerStatistics(player).getBountyKills()),
                                new InternalPlaceholders("%nonbkills%",handler.getPlugin().getDataManager().getStatisticsManager().getPlayerStatistics(player).getKills()),
                                new InternalPlaceholders("%deaths%",handler.getPlugin().getDataManager().getStatisticsManager().getPlayerStatistics(player).getDeaths()),
                                new InternalPlaceholders("%kd%",handler.getPlugin().getDataManager().getStatisticsManager().getPlayerStatistics(player).getKDRatio())
                                ))
                        )
                        .asGuiItem();


                gui.setItem(handler.getPlugin().getConfiguration().getBountyMenuStatsButton().getSlot(),statsItem);
                handler.getPlugin().getDataManager().getOnlineBounties().forEachRemaining(bounty -> {
                    final InternalPlaceholders namePlaceholders = new InternalPlaceholders("%name%",bounty.getPlayer().get().getName());
                    final GuiItem item = ItemBuilder.skull()
                            .owner(bounty.getPlayer().get())
                            .name(MessageFormatter.transform(handler.getPlugin().getConfiguration().getBountyMenuBountyButton().getName(namePlaceholders)))
                            .lore(MessageFormatter.transform(
                                    handler.getPlugin().getConfiguration().getBountyMenuBountyButton().getLore(
                                            new InternalPlaceholders("%time-left%",bounty.getFormattedRemaining()),
                                            new InternalPlaceholders("%kills%",bounty.getKilled()),
                                            new InternalPlaceholders("%xp%",handler.getPlugin().getConfiguration().getXPForKills(bounty.getKilled())),
                                            new InternalPlaceholders("%rand_loc%", LocationUtils.getRandomLocationFromARadius(bounty.getPlayer().get().getLocation(),handler.getPlugin().getConfiguration().getRadiusForRandLocOnKills(bounty.getKilled()))),
                                            namePlaceholders
                                    ))
                            )
                            .asGuiItem();

                    gui.addItem(item);
                });

                return gui;
            }
        });
    }

    @Override
    public String name() {
        return "Bounty Menu";
    }
}
