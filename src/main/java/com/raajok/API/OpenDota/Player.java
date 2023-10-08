package com.raajok.API.OpenDota;

public class Player {

    private int accountId;
    private String avatarFull;
    private String name;
    private String lastMatchTime;
    private Number similarity;

    public Player(int accountId, String avatarFull, String name, String lastMatchTime, Number similarity) {
        this.accountId = accountId;
        this.avatarFull = avatarFull;
        this.name = name;
        this.lastMatchTime = lastMatchTime;
        this.similarity = similarity;
    }

    public int getAccountId() {
        return this.accountId;
    }

    public String getAvatarFull() {
        return this.avatarFull;
    }

    public String getName() {
        return this.name;
    }

    public String getLastMatchTime() {
        return this.lastMatchTime;
    }

    public Number getSimilarity() {
        return this.similarity;
    }

    @Override
    public String toString() {
        return this.name + ", " + this.accountId + ", " + this.avatarFull;
    }
}
