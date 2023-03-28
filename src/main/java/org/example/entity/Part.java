package org.example.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Part<E> {
    private final E type;
    private final String part;
}
