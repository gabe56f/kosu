package hu.gabe.kosu.auth

import hu.gabe.kosu.Kosu

class LegacyHandler(kosu: Kosu, internal val token: String) {
    val client = kosu.newClient {
        addInterceptor {
            val request = it.request()
            it.proceed(
                request.newBuilder().url(request.url.toString().replace("auth_token_here", token))
                    .removeHeader("x-api-version").build()
            )
        }
    }
}