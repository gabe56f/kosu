package hu.gabe.kosu.objects.api.users

import hu.gabe.kosu.annotations.KosuVisitable
import hu.gabe.kosu.child.KosuChild
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@KosuVisitable
data class CurrentUserAttributes(
    @SerialName("can_destroy") val canDestroy: Boolean? = null,
    @SerialName("can_reopen") val canReopen: Boolean? = null,
    @SerialName("can_moderate_kudosu") val canModerateKudosu: Boolean? = null,
    @SerialName("can_resolve") val canResolve: Boolean? = null,
    @SerialName("vote_score") val voteScore: Int? = null,

    @SerialName("can_message") val canMessage: Boolean? = null,
    @SerialName("can_message_error") val cantMessageReason: String? = null,
    @SerialName("last_read_id") val lastReadId: Long? = null,

    @SerialName("can_new_comment_reason") val cantCommentReason: String? = null
) : KosuChild() {
    fun asBeatmapsetPermissions(): BeatmapsetDiscussionPermissions = BeatmapsetDiscussionPermissions(
        canDestroy!!,
        canReopen!!,
        canModerateKudosu!!,
        canResolve!!,
        voteScore!!
    ).also { it.kosu = kosu }

    fun asChatChannelPermissions(): ChatChannelUserAttributes =
        ChatChannelUserAttributes(canMessage!!, lastReadId!!, cantMessageReason).also { it.kosu = kosu }
}

@Serializable
data class BeatmapsetDiscussionPermissions(
    @SerialName("can_destroy") val canDestroy: Boolean,
    @SerialName("can_reopen") val canReopen: Boolean,
    @SerialName("can_moderate_kudosu") val canModerateKudosu: Boolean,
    @SerialName("can_resolve") val canResolve: Boolean,
    @SerialName("vote_score") val voteScore: Int
) : KosuChild()

@Serializable
data class ChatChannelUserAttributes(
    @SerialName("can_message") val canMessage: Boolean,
    @SerialName("last_read_id") val lastReadId: Long,
    @SerialName("can_message_error") val cantMessageReason: String? = null
) : KosuChild()
