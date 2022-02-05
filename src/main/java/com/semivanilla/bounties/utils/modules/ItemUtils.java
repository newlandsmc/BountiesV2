package com.semivanilla.bounties.utils.modules;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.UUID;

public class ItemUtils {


    private static final Material DEFAULT_MATERIAL = Material.GRASS_BLOCK;
    private static final String BASE_HEAD_CODE = "base64:";



    public static ItemStack getHead(@NotNull String texture){
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("texture", texture));
        Field profileField = null;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
            return null;
        }
        profileField.setAccessible(true);
        try {
            profileField.set(skullMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
        skull.setItemMeta(skullMeta);
        return skull;
    }

    public static ItemStack getMaterialFrom(@NotNull String materialNameFetched){
        if(EnumUtils.isValidEnum(Material.class,materialNameFetched)){
            return new ItemStack(Material.getMaterial(materialNameFetched));
        }else {
            if(materialNameFetched.startsWith(BASE_HEAD_CODE)){
                final String textureString = materialNameFetched.substring(BASE_HEAD_CODE.length());
                final ItemStack head = ItemUtils.getHead(textureString);
                if(head == null){
                    Bukkit.getLogger().severe("Unable to fetch the head from the given texture. The default item of "+DEFAULT_MATERIAL.name()+" would be used!");
                    return new ItemStack(DEFAULT_MATERIAL);
                }

                return head;
            }else {
                Bukkit.getLogger().warning("Unable to properly identify the material provided at "+materialNameFetched+". The default item of "+DEFAULT_MATERIAL.name()+" would be used!");
                return new ItemStack(DEFAULT_MATERIAL);
            }
        }
    }

}
