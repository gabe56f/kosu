package hu.gabe.kosu.requests

import hu.gabe.kosu.Kosu
import hu.gabe.kosu.Scope

abstract class BaseRequestV2(val kosu: Kosu) {
    internal val client
        get() = kosu.client
    internal val endpoint by kosu::endpointNew

    @Throws(IllegalStateException::class)
    internal fun canExecute(vararg scopes: Scope = arrayOf(Scope.PUBLIC)) {
        val notIn = scopes.filter { !kosu.auth.scopes.contains(it) }
        if (notIn.isNotEmpty()) throw IllegalStateException("Missing scopes: ${notIn.joinToString(", ") { it.name }}")
    }
}