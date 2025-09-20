package hu.gabe.kosu.objects.api.forum

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForumPost(
    val id: Long,
    @SerialName("created_at") val createdAt: Instant,
    @SerialName("forum_id") val forumId: Long,
    @SerialName("user_id") val userId: Long,
    @SerialName("topic_id") val topicId: Long,
    @SerialName("deleted_at") val deletedAt: Instant? = null,
    @SerialName("edited_at") val editedAt: Instant? = null,
    @SerialName("edited_by_id") val editorId: Long? = null,

    // optional fields
    val body: ForumBody? = null
)

@Serializable
data class ForumBody(val html: String, val raw: String)
