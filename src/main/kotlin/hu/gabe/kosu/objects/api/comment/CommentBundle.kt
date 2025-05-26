package hu.gabe.kosu.objects.api.comment

import hu.gabe.kosu.objects.KosuChild
import hu.gabe.kosu.objects.api.users.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentBundle(
    @SerialName("commentable_meta") val commentableMeta: List<CommentableMeta>,
    val comments: List<Comment>,
    @SerialName("included_comments") val includedComments: List<Comment>,
    val sort: CommentSort,
    // val cursor: Cursor,
    @SerialName("has_more") val hasMore: Boolean,
    @SerialName("user_follow") val userFollowing: Boolean,
    @SerialName("user_votes") val upvoterIds: List<Int>,
    val users: List<User>,
    @SerialName("top_level_count") val topLevelCount: Int? = null,
    val total: Int? = null,
    @SerialName("pinned_comments") val pinnedComments: List<Comment>? = emptyList(),
    @SerialName("has_more_id") val hasMoreid: Long? = null,
) : KosuChild()
