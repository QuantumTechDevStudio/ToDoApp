package quantenpfad.todoapp;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Telegram bot states
 */
@AllArgsConstructor
@Getter
public enum State {

    /**
     * Basic bot state, ready to accept commands
     */
    READY,
    /**
     * New user name has been received
     */
    REGISTER_NAME_ENTERED,
    /**
     * New user surname has been received
     */
    REGISTER_SURNAME_ENTERED,
    ;
}
