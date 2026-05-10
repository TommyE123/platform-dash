package com.platformdash.data

import com.platformdash.domain.Departure
import com.platformdash.domain.DepartureStatus
import com.platformdash.domain.Departures
import com.platformdash.domain.Route
import com.platformdash.domain.TrainRepository
import java.time.Instant

class MockTrainRepository(
    private val scenario: Scenario = Scenario.LBG_TO_RDH_DEFAULT,
) : TrainRepository {

    enum class Scenario {
        LBG_TO_RDH_DEFAULT,
        LBG_TO_RDH_NO_SERVICES,
        LBG_TO_RDH_DELAY_HEAVY,
    }

    override suspend fun getDepartures(route: Route): Departures {
        val normalizedRoute = Route(
            originCode = route.normalizedOrigin,
            destinationCode = route.normalizedDestination,
        )

        if (normalizedRoute != Route.DefaultCommute) {
            return Departures(
                route = normalizedRoute,
                fetchedAt = FIXED_FETCH_TIME,
                services = emptyList(),
            )
        }

        val services = when (scenario) {
            Scenario.LBG_TO_RDH_DEFAULT -> lbgToRdhDefaultServices()
            Scenario.LBG_TO_RDH_NO_SERVICES -> emptyList()
            Scenario.LBG_TO_RDH_DELAY_HEAVY -> lbgToRdhDelayHeavyServices()
        }

        return Departures(
            route = normalizedRoute,
            fetchedAt = FIXED_FETCH_TIME,
            services = services,
        )
    }

    private fun lbgToRdhDefaultServices(): List<Departure> = listOf(
        Departure(
            serviceId = "LBG-RDH-0702",
            destinationCode = "RDH",
            scheduledDepartureIso = "2026-05-10T07:02:00Z",
            expectedDepartureIso = "2026-05-10T07:02:00Z",
            platform = "5",
            status = DepartureStatus.ON_TIME,
        ),
        Departure(
            serviceId = "LBG-RDH-0715",
            destinationCode = "RDH",
            scheduledDepartureIso = "2026-05-10T07:15:00Z",
            expectedDepartureIso = "2026-05-10T07:19:00Z",
            platform = "6",
            status = DepartureStatus.DELAYED,
        ),
        Departure(
            serviceId = "LBG-RDH-0728",
            destinationCode = "RDH",
            scheduledDepartureIso = "2026-05-10T07:28:00Z",
            expectedDepartureIso = "2026-05-10T07:28:00Z",
            platform = "7",
            status = DepartureStatus.CANCELLED,
        ),
    )

    private fun lbgToRdhDelayHeavyServices(): List<Departure> = listOf(
        Departure(
            serviceId = "LBG-RDH-0740",
            destinationCode = "RDH",
            scheduledDepartureIso = "2026-05-10T07:40:00Z",
            expectedDepartureIso = "2026-05-10T07:52:00Z",
            platform = "5",
            status = DepartureStatus.DELAYED,
        ),
        Departure(
            serviceId = "LBG-RDH-0753",
            destinationCode = "RDH",
            scheduledDepartureIso = "2026-05-10T07:53:00Z",
            expectedDepartureIso = "2026-05-10T08:02:00Z",
            platform = null,
            status = DepartureStatus.DELAYED,
        ),
        Departure(
            serviceId = "LBG-RDH-0806",
            destinationCode = "RDH",
            scheduledDepartureIso = "2026-05-10T08:06:00Z",
            expectedDepartureIso = "2026-05-10T08:06:00Z",
            platform = "8",
            status = DepartureStatus.CANCELLED,
        ),
    )

    private companion object {
        val FIXED_FETCH_TIME: Instant = Instant.parse("2026-05-10T07:00:00Z")
    }
}