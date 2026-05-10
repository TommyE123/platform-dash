package com.platformdash.data.repository

import java.time.OffsetDateTime

/**
 * Immutable domain model for a single train departure entry.
 *
 * Both [aimedDepartureTime] and [expectedDepartureTime] are represented as
 * [OffsetDateTime] to preserve explicit timezone offset information. This keeps
 * departure calculations and display behavior deterministic across locales.
 *
 * @property aimedDepartureTime Scheduled departure time from the timetable.
 * @property expectedDepartureTime Real-time expected departure time.
 * @property platform Platform identifier (for example "3" or "B").
 * @property destination Destination station name or code.
 * @property status Current operational state of the service.
 * @property notes Optional free-text service note (for example disruption or
 * platform-change context).
 *
 * @throws IllegalArgumentException if [platform] is blank.
 * @throws IllegalArgumentException if [destination] is blank.
 * @throws IllegalArgumentException if [notes] exceeds 200 characters.
 */
data class TrainDeparture(
    val aimedDepartureTime: OffsetDateTime,
    val expectedDepartureTime: OffsetDateTime,
    val platform: String,
    val destination: String,
    val status: DepartureStatus,
    val notes: String? = null
) {
    init {
        require(platform.isNotBlank()) { "platform must not be blank" }
        require(destination.isNotBlank()) { "destination must not be blank" }
        require(notes == null || notes.length <= 200) { "notes must be 200 characters or fewer" }
    }
}
