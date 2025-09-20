package hu.gabe.kosu.requests

import hu.gabe.kosu.Kosu

abstract class BaseRequestV1(val kosu: Kosu) {
    internal val client
        get() = kosu.legacyHandler.client
    internal val endpoint by kosu::endpoint

    @Throws(IllegalStateException::class)
    internal fun validate() {
        if (!kosu.legacyReady) throw IllegalStateException("Legacy endpoint is not authenticated! Authenticate with Kosu#authenticate(token) first!")
    }

    @Throws(IllegalArgumentException::class, IllegalStateException::class)
    internal fun inRange(value: Int?, start: Int, end: Int, paramName: String) {
        validate()
        if ((value
                ?: 5) !in start..end
        ) throw IllegalArgumentException("$paramName must be between $start and $end (inclusive)")
    }
}