package com.raajok.commands;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.List;

/**
 * Group used by some commands that are personalized for my channel.
 */
public class Leipagang {

    private final List<Integer> idList;

    public Leipagang() {
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

    public List<Integer> ids() {
        return idList;
    }
}
