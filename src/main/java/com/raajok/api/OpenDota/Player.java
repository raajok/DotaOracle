package com.raajok.api.OpenDota;

public class Player {

    private int accountId;
    private String avatarFull;
    private String name;
    private String lastMatchTime;

    public Player(int accountId, String avatarFull, String name, String lastMatchTime) {
        this.accountId = accountId;
        this.avatarFull = avatarFull;
        this.name = name;
        this.lastMatchTime = lastMatchTime;
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

    @Override
    public String toString() {
        return this.name + ", " + this.accountId + ", " + this.avatarFull;
    }
}
