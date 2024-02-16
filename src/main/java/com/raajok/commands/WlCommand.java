package com.raajok.commands;

import com.raajok.api.OpenDota.Player;
import com.raajok.api.OpenDota.OpenDotaAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WlCommand implements Command {

    private List<OptionData> optionList = new ArrayList<>();

    public WlCommand() {
        this.optionList.add(new OptionData(OptionType.INTEGER, "id", "The Steam ID of the player.", true));
        this.optionList.add(new OptionData(OptionType.INTEGER, "amount", "Optional: the amount of matches to limit to.", false));
    }

    @Override
    public String getName() {
        return "wl";
    }

    @Override
    public String getDescription() {
        return "Win/Loss statistics.";
    }

    @Override
    public List<OptionData> getOptions() {
        return this.optionList;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        int amount = 0;
        boolean amountIsNull = true;
        if (event.getOption("amount") != null) {
            amount = event.getOption("amount").getAsInt();
            amountIsNull = false;
        }
        int id = event.getOption("id").getAsInt();

        // Win/Loss statistics
        Map<String, Integer> wl = OpenDotaAPI.wl(id, amount, new ArrayList<>());
        int wins = wl.get("wins");
        int losses = wl.get("losses");
        Double winPercent = ((double) wins) / (((double) wins) + ((double) losses)) * 100;

        // Player information for response
        Player player = OpenDotaAPI.searchPlayer(id);
        DecimalFormat df = new DecimalFormat("####0.00");

        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(player.getName(), null, player.getAvatarFull());

        if (!amountIsNull) {
            embed.setDescription("Win/Loss statistics for the last " + amount + " matches.");
        }

        embed.addField("Wins", Integer.toString(wins), true);
        embed.addField("Losses", Integer.toString(losses), true);
        embed.addField("Win %", df.format(winPercent), true);

        event.replyEmbeds(embed.build()).queue();
    }
}
