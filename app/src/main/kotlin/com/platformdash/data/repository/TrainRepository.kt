package com.platformdash.data.repository

/**
 * Contract for retrieving train departures for an origin-destination route.
 *
 * Implementations must keep this API stable so callers can swap data sources
 * (for example mock vs live API) without changing consumer code.
 */
interface TrainRepository {
    /**
     * Returns the next departures for a route.
     *
     * @param origin Origin station code or display name.
     * @param destination Destination station code or display name.
     * @return A list of [TrainDeparture] items for the route. Implementations
     * should return an empty list if the route is unsupported or no departures
     * are currently available.
     */
    fun getNextDepartures(origin: String, destination: String): List<TrainDeparture>
}
