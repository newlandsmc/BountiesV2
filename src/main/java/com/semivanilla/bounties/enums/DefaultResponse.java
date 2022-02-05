package com.semivanilla.bounties.enums;

import com.semivanilla.bounties.utils.modules.MessageFormatter;
import net.kyori.adventure.text.Component;

public enum DefaultResponse {
    NO_ACTIVE_BOUNTIES(MessageFormatter.transform("<red>There are no active bounties on the server!")),
    INVALID_ARGS(MessageFormatter.transform("<red>The args provided are invalid.")),
    INVALID_PLAYER_ARGS(MessageFormatter.transform("<red>You should provide a player!")),
    ALREADY_HAS_BOUNTY(MessageFormatter.transform("<red>The player is already a bounty!")),
    PLAYER_IS_EXEMPTED_FROM_BOUNTY(MessageFormatter.transform("<red>The player specified is exempted from placing bounties")),
    SUCCESSFULLY_CREATED_BOUNTY(MessageFormatter.transform("<green>The bounty has been created successfully.")),
    IS_NOT_A_BOUNTY(MessageFormatter.transform("<red>The specified player is not a bounty!")),
    SUCCESSFULLY_REMOVED_BOUNTY(MessageFormatter.transform("<green>Cleared the bounty on the player's head")),
    PLAYER_ALREADY_EXEMPTED(MessageFormatter.transform("<red>This player is already exempted from bounties")),
    PLAYER_ALREADY_NOT_EXEMPTED(MessageFormatter.transform("<red>This player is already not exempted from bounties")),
    SUCCESSFULLY_EXEMPTED_FROM_BOUNTY(MessageFormatter.transform("<green>The player has been successfully exempted from the bounty system.")),
    SUCCESSFULLY_REMOVED_FROM_EXEMPT(MessageFormatter.transform("<green>The player has been removed from exempted list.")),
    CANNOT_EXEMPT_ON_ACTIVE_BOUNTY(MessageFormatter.transform("<red>Sorry! The player has a bounty on his head. Its not possible to exempt him now")),
    SUCCESSFULLY_SET_KILLS(MessageFormatter.transform("<green>You have successfully set the kills on the player")),
    PLUGIN_RELOADED(MessageFormatter.transform("<green>Plugin has been reloaded!")),

    UNKNOWN_PLAYER_ON_STATS(MessageFormatter.transform("<red>Failed to set stats on player. Seems like data is missing")),
    SET_STATS_OF_BKILLS(MessageFormatter.transform("<green>You have successfully set the bounty kills on the player")),
    SET_STATS_OF_KILLS(MessageFormatter.transform("<green>You have successfully set the kills on the player")),
    SET_STATS_OF_DEATH(MessageFormatter.transform("<green>You have successfully set the deaths on the player")),
    STATS_RESET(MessageFormatter.transform("<green>You have successfully reset-ed the player statistics data for the player")),

    STATUS_EXEMPTED(MessageFormatter.transform("<green>The player is exempted from the bounty")),
    STATUS_NOT_EXEMPTED(MessageFormatter.transform("<green>The player is not exempted from the bounty"))
    ;

    private final Component response;

    DefaultResponse(Component response) {
        this.response = response;
    }

    public Component getResponse() {
        return response;
    }
}
