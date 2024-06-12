package com.raajok.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Command to print out a list of the bot's commands and descriptions of them.
 */
public class HelpCommand implements Command {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "List of commands and their descriptions.";
    }

    @Override
    public List<OptionData> getOptions() {
        return new ArrayList<>();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String description = "Below is a list of all commands with descriptions. Required parameters are in square brackets [] and optional parameters are in curly brackets {}.";

        Map<String, String> commandDescriptions = new HashMap<>();
        commandDescriptions.put("help", "Opens up this info message");
        commandDescriptions.put("herostats [hero] [id]", "Shows hero-specific stats for the player owning the Steam ID given.");
        commandDescriptions.put("leipägang", "Shows Leipägang's win/loss statistics and brief statistics for all members individually.");
        commandDescriptions.put("records", "Shows the records of Leipägang. The records are taken from the Dotabuff's record page.");
        commandDescriptions.put("search [name]", "Searches players by name and gives top 5 results and their Steam IDs. Mostly used for copy-pasting SteamIDs for other commands.");
        commandDescriptions.put("wl [id] {amount}", "Shows wins, losses and win percent for the player owning the Steam ID given. Can be limited to {amount} of recent games.");

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Help");
        embed.setAuthor("DotaOracle");
        embed.setDescription(description);
        for (String key : commandDescriptions.keySet()) {
            embed.addField(key, commandDescriptions.get(key), false);
        }

        event.replyEmbeds(embed.build()).queue();
    }
}
