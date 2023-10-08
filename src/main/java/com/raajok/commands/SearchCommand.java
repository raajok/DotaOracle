package com.raajok.commands;

import com.raajok.API.OpenDota.Player;
import com.raajok.API.OpenDotaAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class SearchCommand implements Command {

    private List<OptionData> optionList = new ArrayList<>();

    public SearchCommand() {
        this.optionList.add(new OptionData(OptionType.STRING, "name", "The name of the player."));
    }

    @Override
    public String getName() {
        return "search";
    }

    @Override
    public String getDescription() {
        return "Search a player.";
    }

    @Override
    public List<OptionData> getOptions() {
        return this.optionList;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        List<Player> playerList = OpenDotaAPI.searchPlayer(event.getOption("name").getAsString());

        // Send first 5 players as embed messages to the channel.
        List<MessageEmbed> embedList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Player player = playerList.get(i);

            EmbedBuilder embed = new EmbedBuilder();
            embed.setThumbnail(player.getAvatarFull());
            embed.setTitle(player.getName());
            embed.setDescription("Steam id: " + player.getAccountId());

            embedList.add(embed.build());
        }
        event.replyEmbeds(embedList).setEphemeral(true).queue();
    }
}
