package ru.todoapp.model.dto;

/**
 * Request Type
 */
public enum RequestType {
    /**
     * Test Request - Ping
     */
    PING,

    /**
     * Registration request
     */
    REGISTER,

    /**
     * Request for adding a task
     */
    ADD_TASK,

    /**
     * Request for fetching tasks
     */
    FETCH_TASKS
}
