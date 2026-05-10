package com.platformdash.data.repository

/**
 * Represents the real-time operational state of a train departure.
 *
 * This enum is part of the core repository contract and is used by UI layers to
 * render clear service-state feedback for commuters.
 */
enum class DepartureStatus {
    /**
     * The service is running to schedule.
     */
    ON_TIME,

    /**
     * The service is still running, but expected to depart later than scheduled.
     */
    DELAYED,

    /**
     * The service will not run.
     */
    CANCELLED
}
