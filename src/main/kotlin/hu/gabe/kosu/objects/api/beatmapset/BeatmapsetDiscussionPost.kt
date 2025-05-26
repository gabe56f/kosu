package hu.gabe.kosu.objects.api.beatmapset

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BeatmapsetDiscussionPost(
    val id: Long,
    val system: Boolean,
    val message: String,
    @SerialName("created_at") val createdAt: Instant,
    @SerialName("updated_at") val updatedAt: Instant,
    @SerialName("user_id") val userId: Long,
    @SerialName("beatmapset_discussion_id") val beatmapsetDiscussionId: Long,
    @SerialName("last_editor_id") val lastEditorId: Long? = null,
    @SerialName("deleted_at") val deletedAt: Instant? = null,
    @SerialName("deleted_by_id") val deletedById: Long? = null,
)
