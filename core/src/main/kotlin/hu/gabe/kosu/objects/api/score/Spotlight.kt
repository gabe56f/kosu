package hu.gabe.kosu.objects.api.score

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Spotlight(
    val id: Long,
    val name: String,
    val type: String,
    @SerialName("start_date") val start: Instant,
    @SerialName("end_date") val end: Instant,
    @SerialName("mode_specific") val modeSpecific: Boolean,
    @SerialName("participant_count") val participants: Int? = null,
)

@Serializable
data class Spotlights(
    val spotlights: List<Spotlight>
)
