package com.raajok.api.OpenDota;

import com.raajok.api.EpochTime;

/**
 * A wrapper for data from "/peers" OpenDotaAPI call
 */
public class Peer {

    private Player player;
    private EpochTime lastPlayed;
    private int games;
    private int wins;
    // TODO: add rest of the datafields given in the API

    public Peer(Player player, EpochTime lastPlayed, int games, int wins) {
        this.player = player;
        this.lastPlayed = lastPlayed;
        this.games = games;
        this.wins = wins;
    }

    public Player getPlayer() {
        return this.player;
    }

    public EpochTime getLastPlayed() {
        return this.lastPlayed;
    }

    public int getGames() {
        return this.games;
    }

    public int getWins() {
        return this.wins;
    }
}
