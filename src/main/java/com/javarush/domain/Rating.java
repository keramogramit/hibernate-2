package com.javarush.domain;

public enum Rating {
    G("G"),
    PG("PG"),
    PG13("PG-13"),
    R("R"),
    NC17("NC-17");

    public final String name;

    Rating(String name) {
        this.name = name;
    }
}
