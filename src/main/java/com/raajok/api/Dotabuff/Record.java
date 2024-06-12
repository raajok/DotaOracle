package com.raajok.api.Dotabuff;

public interface Record<T> {

    String title();
    String author();
    T value();
    boolean isGreater(Record record);
}
