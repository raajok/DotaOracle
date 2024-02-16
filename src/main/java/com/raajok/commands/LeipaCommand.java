package com.raajok.commands;

import com.raajok.api.DatafeedAPI;
import com.raajok.api.EpochTime;
import com.raajok.api.OpenDota.Hero;
import com.raajok.api.OpenDota.OpenDotaAPI;
import com.raajok.api.OpenDota.Peer;
import com.raajok.api.OpenDota.Player;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

/**
 * Slash command for showing statistics of Leipägang members for the last 6 months.
 */
public class LeipaCommand implements Command {

    private final List<Integer> idList;

    public LeipaCommand() {
        this.idList = new ArrayList<>();

        Dotenv dotenv = Dotenv.load();
        final int PAAHTIS_ID = Integer.parseInt(dotenv.get("PAAHTIS_ID"));
        final int PATONKI_ID = Integer.parseInt(dotenv.get("PATONKI_ID"));
        final int KAURATYYNY_ID = Integer.parseInt(dotenv.get("KAURATYYNY_ID"));
        final int LIMPPU_ID = Integer.parseInt(dotenv.get("LIMPPU_ID"));

        this.idList.add(PAAHTIS_ID);
        this.idList.add(PATONKI_ID);
        this.idList.add(KAURATYYNY_ID);
        this.idList.add(LIMPPU_ID);
    }

    @Override
    public String getName() {
        return "leipägang";
    }

    @Override
    public String getDescription() {
        return "View statistics for the Leipägang members.";
    }

    @Override
    public List<OptionData> getOptions() {
        return new ArrayList<>();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        // Tell Discord to wait, only 3 seconds to respond to the message without deferring
        event.deferReply().queue();

        // Put all messages into a list
        List<MessageEmbed> embedList = new ArrayList<>();

        // Get wl for one of the players, but set query parameters to only include games with all other members.
        // This way we get statistics for the whole group as a whole.
        List<Integer> paramIds = this.idList.subList(1, this.idList.size());
        List<Peer> peers = OpenDotaAPI.peers(this.idList.get(0), paramIds);
        Peer peer = peers.get(0);
        EpochTime lastPlayedTogether = peer.getLastPlayed();
        int games = peer.getGames();
        int wins = peer.getWins();

        // TODO: change to the correct URL
        final String breadThumbnail = "https://dc49c1.hostroomcdn.com/wp-content/uploads/2018/10/2301.jpg";

        EmbedBuilder embed = new EmbedBuilder();
        embed.setThumbnail(breadThumbnail);
        embed.setAuthor("Leipägang");
        embed.setDescription("Leipägang last played together on " + lastPlayedTogether.asDate() + ".");

        embed.addField("Games", String.valueOf(games), true);
        embed.addField("Wins", String.valueOf(wins), true);

        // Add the group statistics as the first message
        embedList.add(embed.build());

        // Build individual messages for all members
        for (int id: this.idList) {
            Player player = OpenDotaAPI.searchPlayer(id);
            List<Hero> heroList = OpenDotaAPI.heroes(id, "?date=180");
            Hero mostPlayedHero = heroList.get(0);
            final String mostPlayedHeroName = DatafeedAPI.getNameFromId(mostPlayedHero.getId());
            final String mostPlayedNpcName = DatafeedAPI.getIdAndNpcNameFromName(mostPlayedHeroName).get(1);
            final String mostPlayedThumbnail = "https://dotabase.dillerm.io/vpk/panorama/images/heroes/" + mostPlayedNpcName + "_png.png";

            int totalGames = 0;
            for (Hero hero: heroList) {
                totalGames += hero.getGames();
            }

            embed = new EmbedBuilder();
            // player info
            embed.setThumbnail(player.getAvatarFull());
            embed.setAuthor(player.getName());
            embed.setTitle("Played " + totalGames + " games in last 6 months.");
            // hero info
            embed.setDescription("Current signature hero is " + mostPlayedHeroName
                    + ". Hero performance in last 6 months below!");
            embed.setImage(mostPlayedThumbnail);
            embed.addField("Games", String.valueOf(mostPlayedHero.getGames()), true);
            embed.addField("Wins", String.valueOf(mostPlayedHero.getWins()), true);
            embed.addField("Last played", mostPlayedHero.getLastPlayed().asDate(), true);

            embedList.add(embed.build());
        }

        event.getHook().editOriginalEmbeds(embedList).queue();
    }
}
