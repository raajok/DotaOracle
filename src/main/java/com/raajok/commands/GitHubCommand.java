package com.raajok.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

/**
 * Command for printing a link to the bots GitHub repository.
 */
public class GitHubCommand implements Command {

    private final String gitHubURL = "https://github.com/raajok/DotaOracle";
    private final String githubLogo = "https://cdn.pixabay.com/photo/2022/01/30/13/33/github-6980894_1280.png";

    @Override
    public String getName() {
        return "github";
    }

    @Override
    public String getDescription() {
        return "Prints the link to the bots GitHub repository, where its source code can be found.";
    }

    @Override
    public List<OptionData> getOptions() {
        return new ArrayList<>();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("GitHub");
        embed.setThumbnail(githubLogo);
        embed.setDescription("DotaOracle's source code can be found here: " + gitHubURL);

        event.replyEmbeds(embed.build()).queue();
    }
}
