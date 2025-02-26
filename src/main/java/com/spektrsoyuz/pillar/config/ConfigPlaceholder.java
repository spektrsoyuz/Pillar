package com.spektrsoyuz.pillar.config;

import lombok.Getter;

@Getter
public class ConfigPlaceholder {

    private final String name;
    private final String value;

    public ConfigPlaceholder(final String name, final String value) {
        this.name = name;
        this.value = value;
    }
}
