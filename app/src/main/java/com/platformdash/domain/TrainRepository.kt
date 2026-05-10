package com.platformdash.domain

interface TrainRepository {
    suspend fun getDepartures(route: Route): Departures
}