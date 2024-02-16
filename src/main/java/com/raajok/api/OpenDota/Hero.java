package com.raajok.api.OpenDota;

import com.raajok.api.Embeddable;
import com.raajok.api.EpochTime;
import net.dv8tion.jda.api.EmbedBuilder;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Hero implements Embeddable {

    private int id;
    private EpochTime lastPlayed;
    private int games;
    private int wins;
    private int withGames;
    private int withWins;
    private int againstGames;
    private int againstWins;

    public Hero(int id, EpochTime lastPlayed, int games, int wins, int withGames, int withWins, int againstGames, int againstWins) {
        this.id = id;
        this.lastPlayed = lastPlayed;
        this.games = games;
        this.wins = wins;
        this.withGames = withGames;
        this.withWins = withWins;
        this.againstGames = againstGames;
        this.againstWins = againstWins;
    }

    public Hero() {

    }

    public int getId() {
        return id;
    }

    public EpochTime getLastPlayed() {
        return lastPlayed;
    }

    public int getGames() {
        return games;
    }

    public int getWins() {
        return wins;
    }

    public int getWithGames() {
        return withGames;
    }

    public int getWithWins() {
        return withWins;
    }

    public int getAgainstGames() {
        return againstGames;
    }

    public int getAgainstWins() {
        return againstWins;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLastPlayed(EpochTime lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setWithGames(int withGames) {
        this.withGames = withGames;
    }

    public void setWithWins(int withWins) {
        this.withWins = withWins;
    }

    public void setAgainstGames(int againstGames) {
        this.againstGames = againstGames;
    }

    public void setAgainstWins(int againstWins) {
        this.againstWins = againstWins;
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
        String dateString = this.lastPlayed.asDate();

        NumberFormat formatter = new DecimalFormat("#0.0");

        builder.addField("Last played", dateString, false);
        builder.addField("Games", Integer.toString(this.games), true);
        builder.addField("Win %", formatter.format(Double.valueOf(this.wins) / Double.valueOf(this.games) * 100) + " %", true);
        builder.addBlankField(true);
        builder.addField("Wins", Integer.toString(this.wins), true);
        builder.addField("Losses", Integer.toString(this.games - this.wins), true);
        builder.addBlankField(true);
        builder.addField("Games played with", Integer.toString(this.withGames), true);
        builder.addField("Win % with", formatter.format(Double.valueOf(this.withWins) / Double.valueOf(this.withGames) * 100) + " %", true);
        builder.addBlankField(true);
        builder.addField("Games played against", Integer.toString(this.againstGames), true);
        builder.addField("Win % against", formatter.format(Double.valueOf(this.againstWins) / Double.valueOf(this.againstGames) * 100) + " %", true);
        builder.addBlankField(true);

        return builder;
    }
}
