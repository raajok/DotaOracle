package com.raajok.api.OpenDota;

import com.raajok.api.Embeddable;
import net.dv8tion.jda.api.EmbedBuilder;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Hero implements Embeddable {

    private int id;
    private int lastPlayed;
    private int games;
    private int wins;

    public Hero(int id, int lastPlayed, int games, int wins) {
        this.id = id;
        this.lastPlayed = lastPlayed;
        this.games = games;
        this.wins = wins;
    }

    public int getId() {
        return id;
    }

    public int getLastPlayed() {
        return lastPlayed;
    }

    public int getGames() {
        return games;
    }

    public int getWins() {
        return wins;
    }

    @Override
    public EmbedBuilder embed() {
        return addFields(new EmbedBuilder());
    }

    @Override
    public EmbedBuilder embed(EmbedBuilder embedBuilder) {
        return addFields(embedBuilder);
    }

    private EmbedBuilder addFields(EmbedBuilder builder) {
        Date date = new Date(lastPlayed * 1000L);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        format.setTimeZone(TimeZone.getDefault());
        String dateString = format.format(date);

        NumberFormat formatter = new DecimalFormat("#0.0");

        builder.addField("Last played", dateString, true);
        builder.addField("Games", Integer.toString(this.games), true);
        builder.addField("Wins", Integer.toString(this.wins), true);
        builder.addField("Losses", Integer.toString(this.games - this.wins), true);
        builder.addField("Win %", formatter.format(Double.valueOf(this.wins) / Double.valueOf(this.games) * 100), true);

        return builder;
    }
}
