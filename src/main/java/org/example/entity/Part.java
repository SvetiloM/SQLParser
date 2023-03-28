package org.example.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Part {
    private final PartType type;
    private final String part;
}
