package com.javarush.domain;

import static java.util.Objects.isNull;

public enum Features {
    TRAILERS("Trailers"),
    COMMENTARIES("Commentaries"),
    DELETED_SCENES("Deleted Scenes"),
    BEHIND_THE_SCENES("Behind the Scenes");

    private final String name;


    Features(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Features getFeatureByValue(String value) {
        if (isNull(value) || value.isEmpty()){
            return null;
        }
        Features[] features = Features.values();
        for (Features feature : features) {
            if (feature.getName().equals(value)) {
                return feature;
            }
        }
        return null;
    }
}
