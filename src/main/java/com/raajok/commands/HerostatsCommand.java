package com.raajok.commands;

import com.raajok.api.DatafeedAPI;
import com.raajok.api.OpenDota.Hero;
import com.raajok.api.OpenDota.OpenDotaAPI;
import com.raajok.api.OpenDota.Totals;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

/**
 * Command for searching player's all-time stats for a single hero.
 */
public class HerostatsCommand implements Command {

    private List<OptionData> optionList = new ArrayList<>();

    public HerostatsCommand() {
        this.optionList.add(new OptionData(OptionType.STRING, "hero", "The name of the hero."));
        this.optionList.add(new OptionData(OptionType.INTEGER, "id", "The player's Steam ID."));
    }

    @Override
    public String getName() {
        return "herostats";
    }

    @Override
    public String getDescription() {
        return "Get player's stats for a specified hero.";
    }

    @Override
    public List<OptionData> getOptions() {
        return this.optionList;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        // Tell Discord to wait, only 3 seconds to respond to the message without deferring
        event.deferReply().queue();

        String heroName = event.getOption("hero").getAsString();
        int accountId = event.getOption("id").getAsInt();

        // Get hero id for preparation to other API calls
        List<String> idAndName;
        try {
            idAndName = DatafeedAPI.getIdAndNpcNameFromName(heroName);
        } catch (IllegalArgumentException e) {
            event.reply("There is no hero called " + heroName).setEphemeral(true).queue();
            return;
        }
        int heroId = Integer.parseInt(idAndName.get(0));
        heroName = idAndName.get(2);

        // build URLs
        String thumbnailUrl = "https://dotabase.dillerm.io/vpk/panorama/images/heroes/" + idAndName.get(1) + "_png.png";
        String avatarUrl = "https://dotabase.dillerm.io/vpk/panorama/images/heroes/icons/" + idAndName.get(1) + "_png.png";
        String heroUrl = "https://dota2.com/hero/" + heroName.toLowerCase().replaceAll("\\s+", "");

        // get basic hero statistics
        Hero hero = OpenDotaAPI.hero(heroId, accountId);
        // get more specific statistics
        Totals totals = OpenDotaAPI.totals(heroId, accountId);

        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(heroName, heroUrl, avatarUrl);
        embed.setThumbnail(thumbnailUrl);
        embed = hero.embed(embed);
        embed = totals.embed(embed);

        event.getHook().editOriginalEmbeds(embed.build()).queue();
    }
}
