package com.raajok.commands;

import com.raajok.api.Dotabuff.GroupRecords;
import com.raajok.api.Dotabuff.Record;
import com.raajok.api.Dotabuff.Records;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Command for showing records of the Leipägang group, scraped from Dotabuff.
 */
public class RecordsCommand implements Command {

    private final String dotabuffImgURL = "https://crowdin-static.downloads.crowdin.com/images/project-logo/46536/small/c655f23a8817323d78a05e3da7b47ef5171.png";

    @Override
    public String getName() {
        return "records";
    }

    @Override
    public String getDescription() {
        return "Leipägang records in Dota 2.";
    }

    @Override
    public List<OptionData> getOptions() {
        return new ArrayList<>();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        // Tell Discord to wait, only 3 seconds to respond to the message without deferring
        event.deferReply().queue();

        List<Integer> ids = new Leipagang().ids();

        List<Record> groupRecords;
        ArrayList<Records> recordsList = new ArrayList<>();
        ids.forEach(id -> recordsList.add(new Records(id)));
        try {
            groupRecords = new GroupRecords(recordsList).records();
        } catch (IOException e) {
            event.getHook().editOriginal("Sorry, couldn't load records.").queue();
            return;
        }

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Records of Leipägang");
        embed.setDescription("Leipägang's collective Dotabuff records!");
        embed.setThumbnail(dotabuffImgURL);
        for (Record record : groupRecords) {
            embed.addField(record.title(), record.author() + ": " + record.value(), true);
        }

        event.getHook().editOriginalEmbeds(embed.build()).queue();
    }
}
