package hu.gabe.kosu.objects.api

import hu.gabe.kosu.objects.api.users.User
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeasonalBackgrounds(
    @SerialName("ends_at") val endsAt: Instant,
    val backgrounds: List<Background>
)

@Serializable
data class Background(
    val url: String,
    val user: User
)
