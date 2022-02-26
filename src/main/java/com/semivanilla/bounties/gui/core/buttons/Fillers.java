package com.semivanilla.bounties.gui.core.buttons;

import com.google.common.base.Objects;
import com.semivanilla.bounties.utils.modules.MessageFormatter;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Fillers{

    private final ItemStack material;
    private final List<Integer> slotList;

    public Fillers(ItemStack material, List<Integer> slotList) {
        this.material = material;
        this.slotList = slotList;
    }

    public ItemStack getMaterial() {
        return material;
    }

    public List<Integer> getSlotList() {
        return slotList;
    }

    public GuiItem getAsGUIItem(){
        return ItemBuilder.from(this.material).name(Component.empty()).asGuiItem();
    }

    public static Fillers buildFrom(@NotNull ItemStack stack, List<String> slotNo){
        return new Fillers(stack,slotNo.stream().map(Integer::parseInt).toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fillers fillers = (Fillers) o;
        return Objects.equal(material, fillers.material) && Objects.equal(slotList, fillers.slotList);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(material, slotList);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("material", material)
                .append("slotList", slotList)
                .toString();
    }
}
