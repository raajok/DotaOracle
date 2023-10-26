package com.raajok.api.OpenDota;

import com.raajok.api.Embeddable;
import net.dv8tion.jda.api.EmbedBuilder;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Hero specific totals in stats
 */
public class Totals implements Embeddable {

    private int games;
    private int kills;
    private int deaths;
    private int assists;
    private int kda;
    private int goldPerMin;
    private int xpPerMin;
    private int lastHits;
    private int denies;

    public Totals(int games, int kills, int deaths, int assists, int kda, int goldPerMin, int xpPerMin, int lastHits, int denies) {
        this.games = games;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.kda = kda;
        this.goldPerMin = goldPerMin;
        this.xpPerMin = xpPerMin;
        this.lastHits = lastHits;
        this.denies = denies;
    }

    public int getGames() {
        return games;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getAssists() {
        return assists;
    }

    public int getKda() {
        return kda;
    }

    public int getGoldPerMin() {
        return goldPerMin;
    }

    public int getXpPerMin() {
        return xpPerMin;
    }

    public int getLastHits() {
        return lastHits;
    }

    public int getDenies() {
        return denies;
    }

    @Override
    public EmbedBuilder embed() {
        EmbedBuilder builder = new EmbedBuilder();
        builder = addFields(builder);

        return builder;
    }

    @Override
    public EmbedBuilder embed(EmbedBuilder builder) {
        builder = addFields(builder);

        return builder;
    }

    private EmbedBuilder addFields(EmbedBuilder builder) {
        NumberFormat formatter = new DecimalFormat("#0.0");
        NumberFormat noDecimal = new DecimalFormat(("#0"));

        builder.addField("Games", Integer.toString(this.games), true);
        builder.addField("Avg kills", formatter.format(Double.valueOf(this.kills) / Double.valueOf(this.games)) , true);
        builder.addField("Avg Deaths", formatter.format(Double.valueOf(this.deaths) / Double.valueOf(this.games)), true);
        builder.addField("Avg Assists", formatter.format(Double.valueOf(this.assists) / Double.valueOf(this.games)), true);
        builder.addField("Avg KDA", formatter.format(Double.valueOf(this.kda) / Double.valueOf(this.games)), true);
        builder.addField("Avg Gold/Min", noDecimal.format(Double.valueOf(this.goldPerMin) / Double.valueOf(this.games)), true);
        builder.addField("Avg XP/Min", noDecimal.format(Double.valueOf(this.xpPerMin) / Double.valueOf(this.games)), true);
        builder.addField("Avg Lasthits", noDecimal.format(Double.valueOf(this.lastHits) / Double.valueOf(this.games)), true);
        builder.addField("Avg Denies", noDecimal.format(Double.valueOf(this.denies) / Double.valueOf(this.games)), true);

        return builder;
    }
}
