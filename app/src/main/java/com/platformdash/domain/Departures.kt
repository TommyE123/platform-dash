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
) {
    init {
        require(serviceId.isNotBlank()) { "serviceId must not be blank" }
        require(destinationCode.trim().isNotBlank()) { "destinationCode must not be blank" }
        require(scheduledDepartureIso.isNotBlank()) { "scheduledDepartureIso must not be blank" }
        require(expectedDepartureIso.isNotBlank()) { "expectedDepartureIso must not be blank" }
    }
}

data class Departures(
    val route: Route,
    val fetchedAt: Instant,
    val services: List<Departure>,
) {
    init {
        require(route.normalizedOrigin.isNotBlank()) { "route origin must not be blank" }
        require(route.normalizedDestination.isNotBlank()) { "route destination must not be blank" }
    }
}