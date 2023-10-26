package com.raajok.api;

import net.dv8tion.jda.api.EmbedBuilder;

/**
 * Interface for API result objects to provide an EmbedBuilder of themselves or add their fields to a provided EmbedBuilder.
 */
public interface Embeddable {
    EmbedBuilder embed();
    EmbedBuilder embed(EmbedBuilder embedBuilder);
}
