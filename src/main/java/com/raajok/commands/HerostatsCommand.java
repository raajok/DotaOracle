package com.raajok.commands;

import com.raajok.api.DatafeedAPI;
import com.raajok.api.OpenDota.Hero;
import com.raajok.api.OpenDota.OpenDotaAPI;
import com.raajok.api.OpenDota.Player;
import com.raajok.api.OpenDota.Totals;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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
            event.getHook().editOriginal("There is no hero called *" + heroName + "*.").queue();
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
        // search player name from ID
        Player player = OpenDotaAPI.searchPlayer(accountId);
        String playerName = player.getName();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(heroName, heroUrl, avatarUrl);
        embed.setThumbnail(thumbnailUrl);
        embed.setDescription("Player **" + playerName + "** statistics for **" + heroName + "**.");
        embed = embed(embed, hero, totals);

        event.getHook().editOriginalEmbeds(embed.build()).queue();
    }

    /**
     * Turn the Hero and Totals information into an embed message
     * @param builder
     * @param hero
     * @param totals
     * @return Embed message with all info
     */
    private EmbedBuilder embed(EmbedBuilder builder, Hero hero, Totals totals) {
        NumberFormat formatter = new DecimalFormat("#0.0");
        NumberFormat noDecimal = new DecimalFormat(("#0"));

        // check win % fields for 0 values
        MessageEmbed.Field winPercentField;
        MessageEmbed.Field withWinsPercentField;
        MessageEmbed.Field againstWinsPercentField;
        if (hero.getGames() == 0) {
            winPercentField = new MessageEmbed.Field("Win %", "0", true);
        } else {
            winPercentField = new MessageEmbed.Field("Win %",formatter.format(Double.valueOf(hero.getWins()) / Double.valueOf(hero.getGames()) * 100) + " %", true);
        }

        if (hero.getWithGames() == 0) {
            withWinsPercentField = new MessageEmbed.Field("Win % with", "0", true);
        } else {
            withWinsPercentField = new MessageEmbed.Field("Win % with", formatter.format(Double.valueOf(hero.getWithWins()) / Double.valueOf(hero.getWithGames()) * 100) + " %", true);
        }

        if (hero.getAgainstGames() == 0) {
            againstWinsPercentField = new MessageEmbed.Field("Win % against", "0", true);
        } else {
            againstWinsPercentField = new MessageEmbed.Field("Win % against", formatter.format(Double.valueOf(hero.getAgainstWins()) / Double.valueOf(hero.getAgainstGames()) * 100) + " %", true);
        }

        // Basic hero stats
        builder.addField("Last played", hero.getLastPlayed().asDate(), true);
        builder.addField("Time played", totals.getDuration() / 60 / 60 + "h", true);
        builder.addBlankField(true);
        builder.addField("Games", Integer.toString(hero.getGames()), true);
        builder.addField(winPercentField);
        builder.addBlankField(true);
        builder.addField("Wins", Integer.toString(hero.getWins()), true);
        builder.addField("Losses", Integer.toString(hero.getGames() - hero.getWins()), true);
        builder.addBlankField(true);
        builder.addField("Games played with", Integer.toString(hero.getWithGames()), true);
        builder.addField(withWinsPercentField);
        builder.addBlankField(true);
        builder.addField("Games played against", Integer.toString(hero.getAgainstGames()), true);
        builder.addField(againstWinsPercentField);
        builder.addBlankField(true);
        builder.addBlankField(false);

        // Averages
        if (totals.getGames() == 0) {
            builder.addField("Avg kills","N/A" , true);
            builder.addField("Avg Deaths", "N/A", true);
            builder.addField("Avg Assists", "N/A", true);
            builder.addField("Avg KDA", "N/A", true);
            builder.addField("Avg Gold/Min", "N/A", true);
            builder.addField("Avg XP/Min", "N/A", true);
            builder.addField("Avg Lasthits", "N/A", true);
            builder.addField("Avg Denies", "N/A", true);
            builder.addBlankField(true);
        } else {
            builder.addField("Avg kills", formatter.format(Double.valueOf(totals.getKills()) / Double.valueOf(totals.getGames())) , true);
            builder.addField("Avg Deaths", formatter.format(Double.valueOf(totals.getDeaths()) / Double.valueOf(totals.getGames())), true);
            builder.addField("Avg Assists", formatter.format(Double.valueOf(totals.getAssists()) / Double.valueOf(totals.getGames())), true);
            builder.addField("Avg KDA", formatter.format(Double.valueOf(totals.getKda()) / Double.valueOf(totals.getGames())), true);
            builder.addField("Avg Gold/Min", noDecimal.format(Double.valueOf(totals.getGoldPerMin()) / Double.valueOf(totals.getGames())), true);
            builder.addField("Avg XP/Min", noDecimal.format(Double.valueOf(totals.getXpPerMin()) / Double.valueOf(totals.getGames())), true);
            builder.addField("Avg Lasthits", noDecimal.format(Double.valueOf(totals.getLastHits()) / Double.valueOf(totals.getGames())), true);
            builder.addField("Avg Denies", noDecimal.format(Double.valueOf(totals.getDenies()) / Double.valueOf(totals.getGames())), true);
            builder.addBlankField(true);
        }

        return builder;
    }
}
