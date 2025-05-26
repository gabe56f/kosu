package hu.gabe.kosu.objects.api.comment

import hu.gabe.kosu.objects.KosuChild
import hu.gabe.kosu.objects.api.users.CurrentUserAttributes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentableMeta(
    @SerialName("current_user_attributes") val currentUserAttributes: CurrentUserAttributes? = null,
    val id: Long,
    val title: String,
    val type: String,
    val url: String,
    @SerialName("owner_id") val ownerId: Long? = null,
    @SerialName("owner_title") val ownerTitle: String? = null,
) : KosuChild()
