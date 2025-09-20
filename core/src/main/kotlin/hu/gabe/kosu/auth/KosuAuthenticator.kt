package hu.gabe.kosu.auth

import hu.gabe.kosu.Kosu
import hu.gabe.kosu.Scope
import hu.gabe.kosu.objects.api.users.User
import okhttp3.OkHttpClient

abstract class KosuAuthenticator(private val kosu: Kosu) {
    abstract var scopes: List<Scope>
    abstract val client: OkHttpClient
    abstract val token: String?
    abstract val user: User?

    abstract fun deauth(): Boolean
}