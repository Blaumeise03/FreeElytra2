package de.blaumeise03.freeElytra2;

import java.io.IOException;

public class CorruptedConfigurationException extends IOException {
    /**
     * Constructs an {@code IOException} with {@code null}
     * as its error detail message.
     */
    public CorruptedConfigurationException() {
        super("Corrupted configuration file! Can't read data! Please fix it!");
    }

    /**
     * Constructs an {@code IOException} with the specified detail message.
     *
     * @param cause The cause (what is corrupted?)
     */
    public CorruptedConfigurationException(String cause) {
        super("Corrupted configuration file! Can't read data! Please fix it! Cause: " + cause);
    }
}
