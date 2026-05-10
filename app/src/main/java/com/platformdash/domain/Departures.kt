package com.platformdash.domain

import java.time.Instant

enum class DepartureStatus {
    ON_TIME,
    DELAYED,
    CANCELLED,
}

data class Departure(
    val serviceId: String,
    val destinationCode: String,
    val scheduledDepartureIso: String,
    val expectedDepartureIso: String,
    val platform: String?,
    val status: DepartureStatus,
)

data class Departures(
    val route: Route,
    val fetchedAt: Instant,
    val services: List<Departure>,
)