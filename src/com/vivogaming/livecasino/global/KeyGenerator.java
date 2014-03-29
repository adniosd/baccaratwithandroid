package com.vivogaming.livecasino.global;

import java.util.UUID;

public abstract class KeyGenerator {

    public static final String generateGuid() {
        return UUID.randomUUID().toString();
    }

    public static final String generateHash(final String _inputStr) {
        return String.valueOf(_inputStr.hashCode());
    }
}
