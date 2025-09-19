# Kosu

Kosu is a wrapper for the [osu!api.] ([v1] and [v2]).

Since this whole codebase has mostly been completed over a 2-day sprint, there **MAY BE ISSUES!** Feel free to open issues when things don't work as you'd expect them to!

```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.gabe56f:kosu:main-SNAPSHOT")
}
```

### Authentication

Kosu supports 3 ways -- *4, if you count legacy api as well* -- of authenticating.
1. `NoAuth`, which provides very limited access to some osu! endpoints. Doesn't support any scopes.
```kotlin
// Kosu by default initializes a NoAuth authorizer, hence there isn't a need to change anything.
val kosu = Kosu()
println(kosu.authenticated) // false
```

2. `ClientCredentialsGrant`, which gives access to plenty of endpoints, besides the ones requiring various scopes. Supports the `DELEGATE` and `PUBLIC` scopes.
```kotlin
val clientId: Int = ...
val clientSecret: String = ...

// In this case, it is advised to use the builder class, or instantiate the Kosu object with scopes filled out,
// because changing scopes requires reauthenticating.
val kosu = Kosu.Builder {
    scope(Scope.PUBLIC)
}.build().login(clientId, clientSecret)
println(kosu.users["gabe56f"]) // User(...)

// If for whatever reason you'd like to change scopes after auth, you may do so with the following line
// Do keep in mind, that this will invalidate your current session token
kosu.auth.scopes = listOf(Scope.PUBLIC, Scope.DELEGATE)
```

3. `AuthorizationCodeGrant`, the most permissive, but hardest to upkeep authentication method. This one requires running an OAuth server and authenticating the Kosu object with that. Supports every scope.
```kotlin
val clientId: Int = ...
val clientSecret: String = ...
val redirectUri: String = ...

val (kosu, authFlow) = Kosu.Builder {
    scope(Scope.PUBLIC)
    scope(Scope.IDENTIFY)
}.build().login(clientId, clientSecret, redirectUri)

// Acquire oauth token
val token: String = ...
authFlow.code(token)  // Kosu is now authenticated!
```

4. `LegacyAuth`, which provides access to legacy endpoints. V2 authenticated `Kosu` objects can be authenticated for legacy as well.
```kotlin
val clientId: Int = ...
val clientSecret: String = ...
val legacySecret: String = ...

val kosu = Kosu.Builder {
    scope(Scope.PUBLIC)
}.build().login(clientId, clientSecret).authenticate(legacySecret)
// OR
val kosu = Kosu().authenticate(legacySecret)

println(kosu.legacy.users["gabe56f"]) // LegacyUser(...)
```

Deauthenticating is as easy as calling `kosu.deauth()`.

### Endpoints

Most of the endpoints, besides chat *should* be implemented and good to go... if not, feel free to open an issue.

### Usage

```kotlin
import hu.gabe.kosu.Kosu
import hu.gabe.kosu.auth.Scope
import hu.gabe.kosu.objects.api.beatmap.BeatmapStatus

lateinit var kosu: Kosu

fun main() {
    val clientId = 123
    val clientSecret = "secret"
    
    // create a kosu object and authenticate with client credentials
    kosu = Kosu.Builder {
        scope(Scope.PUBLIC)
    }.build().login(clientId, clientSecret)
    
    with(kosu) {
        // get two beatmapsets by id
        val beatmapsets = beatmaps[listOf(2665294, 4445719)] // or beatmaps.get(listOf(...))
        
        // check if they are both ranked
        if (beatmapsets.all { it.status == BeatmapStatus.RANKED }) {
            println("Both maps are ranked!")
        }
    }
}
```

[osu!api.]: https://osu.ppy.sh/docs/index.html
[v2]: https://osu.ppy.sh/docs/index.html
[v1]: https://github.com/ppy/osu-api/wiki