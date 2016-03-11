package com.mobica.ifootball.domain.enumeration;

/**
 * The ParameterKey enumeration.
 */
public enum ParameterKey {
    VIBRATION_AMPLITUDE_THRESHOLD("0.2"),
    VIBRATION_COUNT_THRESHOLD("20"),
    VIBRATION_ANALYSE_INTERVAL("10");

    private final String defaultValue;


    ParameterKey(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
