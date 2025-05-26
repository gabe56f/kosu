package hu.gabe.kosu.objects.api.match

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Match(
    val id: Long,
    val name: String,
    @SerialName("start_time") val startTime: Instant,
    @SerialName("end_time") val endTime: Instant? = null
)
