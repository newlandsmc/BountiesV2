package com.semivanilla.bounties.utils.modules;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.transformation.TransformationRegistry;
import net.kyori.adventure.text.minimessage.transformation.TransformationType;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class MessageFormatter {
    private final static MiniMessage messageFormatter;

    static {
        messageFormatter = MiniMessage.builder()
                .transformations(TransformationRegistry.builder()
                        .add(TransformationType.COLOR)
                        .add(TransformationType.DECORATION)
                        .add(TransformationType.HOVER_EVENT)
                        .add(TransformationType.CLICK_EVENT)
                        .add(TransformationType.KEYBIND)
                        .add(TransformationType.TRANSLATABLE)
                        .add(TransformationType.INSERTION)
                        .add(TransformationType.FONT)
                        .add(TransformationType.GRADIENT)
                        .add(TransformationType.RAINBOW)
                        .build())
                .strict(false)
                .build();
    }

    public static String colorizeLegacy(String message){
        if(StringUtils.isBlank(message))
            return "";
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> colorizeLegacy(@NotNull List<String> message) {
        return message.stream().map(MessageFormatter::colorizeLegacy).collect(Collectors.toList());
    }

    public static Component convertToComponent(String message){
        if(StringUtils.isBlank(message))
            return Component.empty();

        return Component.text(message);
    }

    public static List<Component> convertToComponent(@NotNull List<String> message){
        return message.stream().map(MessageFormatter::convertToComponent).collect(Collectors.toList());
    }

    public static Component convertLegacyToComponent(String message){
        return convertToComponent(colorizeLegacy(message));
    }

    public static List<Component> convertLegacyToComponent(@NotNull List<String> message){
        return convertToComponent(colorizeLegacy(message));
    }

    public static Component transform(String message){
        if(StringUtils.isBlank(message))
            return Component.empty();

        return messageFormatter.deserialize(message);
    }

    public static List<Component> transform(@NotNull List<String> messages){
        return messages.stream().map(MessageFormatter::transform).collect(Collectors.toList());
    }

    public static Component transform(String message, InternalPlaceholders... placeholders){
        String messageToTransform = message;
        for (InternalPlaceholders pl : placeholders){
            messageToTransform = pl.replacePlaceholders(message);
        }
        return transform(messageToTransform);
    }

    public static List<Component> transform(@NotNull List<String> messages, InternalPlaceholders... placeholders){
        List<String> messagesToTransform = new ArrayList<>(messages);
        for(InternalPlaceholders pl: placeholders){
            messagesToTransform = pl.replacePlaceholders(messagesToTransform);
        }
        return transform(messagesToTransform);
    }
}
