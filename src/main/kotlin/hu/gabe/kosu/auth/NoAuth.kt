package hu.gabe.kosu.auth

import hu.gabe.kosu.Kosu
import hu.gabe.kosu.objects.api.users.User

class NoAuth(private val kosu: Kosu) : KosuAuthenticator(kosu) {
    override var scopes: List<Scope>
        get() = emptyList()
        set(value) {
            throw NotImplementedError("You can't set the scope of a non-authenticated client")
        }
    override val client by lazy { kosu.newClient() }
    override val token: String? = null
    override val user: User? = null
    override fun deauth() = throw NotImplementedError(
        "You can't de-authenticate a non-authenticated client"
    )
}