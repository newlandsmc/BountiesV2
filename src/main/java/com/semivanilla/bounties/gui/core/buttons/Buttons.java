package com.semivanilla.bounties.gui.core.buttons;

import com.semivanilla.bounties.utils.modules.InternalPlaceholders;
import com.semivanilla.bounties.utils.modules.ItemUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Buttons{

    private final ItemStack itemStack;
    private final String name;
    private final int slot;
    private final List<String> lore;
    private final int amount;

    public Buttons(ItemStack itemStack, String name, int slot, List<String> lore) {
        this.itemStack = itemStack;
        this.name = name;
        this.slot = slot;
        this.lore = lore;
        this.amount = 1;
    }

    public Buttons(ItemStack itemStack, String name, int slot, List<String> lore, int amount) {
        this.itemStack = itemStack;
        this.name = name;
        this.slot = slot;
        this.lore = lore;
        this.amount = amount;
    }

    public List<String> getLore(InternalPlaceholders... placeholders) {
        List<String> list = lore;
        for(InternalPlaceholders pl : placeholders){
            list = pl.replacePlaceholders(list);
        }
        return list;
    }

    public String getName(InternalPlaceholders... placeholders){
        String returnString = this.name;
        for(InternalPlaceholders pl : placeholders){
            returnString = pl.replacePlaceholders(returnString);
        }
        return returnString;
    }

    public static Buttons buildButtons(@NotNull ConfigurationSection section){
        return new Buttons(
                ItemUtils.getMaterialFrom(section.getString("item")),
                section.getString("name"),
                section.getInt("slot"),
                (section.getStringList("lore")),
                section.getInt("amount")
        );
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getName() {
        return name;
    }

    public int getSlot() {
        return slot;
    }

    public List<String> getLore() {
        return lore;
    }

    public int getAmount() {
        return amount;
    }


}