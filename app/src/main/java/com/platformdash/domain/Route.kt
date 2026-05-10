package com.platformdash.domain

data class Route(
    val originCode: String,
    val destinationCode: String,
) {
    val normalizedOrigin: String = originCode.trim().uppercase()
    val normalizedDestination: String = destinationCode.trim().uppercase()

    fun swap(): Route = Route(
        originCode = normalizedDestination,
        destinationCode = normalizedOrigin,
    )

    companion object {
        val DefaultCommute = Route(originCode = "LBG", destinationCode = "RDH")
    }
}