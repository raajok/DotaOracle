package com.raajok.api.Dotabuff;

public class NumberRecord implements Record<Integer> {

    private final String title;
    private final int value;
    private final String author;

    public NumberRecord(String title, int value, String author) {
        this.title = title;
        this.value = value;
        this.author = author;
    }

    @Override
    public String title() {
        return this.title;
    }

    @Override
    public String author() {
        return this.author;
    }

    @Override
    public Integer value() {
        return this.value;
    }

    @Override
    public boolean isGreater(Record record) {
        try {
            return this.value > Integer.parseInt(record.value().toString());
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
