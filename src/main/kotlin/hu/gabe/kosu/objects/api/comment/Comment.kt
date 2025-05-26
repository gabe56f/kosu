package hu.gabe.kosu.objects.api.comment

import hu.gabe.kosu.objects.KosuChild
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val id: Long,
    val pinned: Boolean,
    @SerialName("commentable_id") val commentableId: Long,
    @SerialName("commentable_type") val commentableType: String,
    @SerialName("created_at") val createdAt: Instant,
    @SerialName("updated_at") val updatedAt: Instant,
    @SerialName("user_id") val userId: Long,
    @SerialName("votes_count") val votes: Int,
    @SerialName("replies_count") val replies: Int,
    val message: String? = null,
    @SerialName("message_id") val messageId: Long? = null,
    @SerialName("legacy_name") val legacyName: String? = null,
    @SerialName("deleted_at") val deletedAt: Instant? = null,
    @SerialName("edited_at") val editedAt: Instant? = null,
    @SerialName("edited_by_id") val editedById: Long? = null,
) : KosuChild()
