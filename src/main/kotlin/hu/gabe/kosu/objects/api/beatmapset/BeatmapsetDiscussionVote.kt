package hu.gabe.kosu.objects.api.beatmapset

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BeatmapsetDiscussionVote(
    val id: Long,
    @SerialName("beatmapset_discussion_id") val beatmapsetDiscussionId: Long,
    val score: Int,
    @SerialName("created_at") val createdAt: Instant,
    @SerialName("updated_at") val updatedAt: Instant,
    @SerialName("user_id") val userId: Int
)
