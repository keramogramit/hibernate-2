package com.javarush.entity;

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

    public String getName() {
        return name;
    }
}
