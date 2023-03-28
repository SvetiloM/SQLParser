package org.example.entity;

public interface PartType {

    String getValue();

    Direction getDirection();

    enum Direction {
        BEFORE,
        AFTER
    }

}
