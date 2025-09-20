package hu.gabe.kosu.objects.api.forum

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForumTopic(
    val id: Long,
    @SerialName("forum_id") val forumId: Long,
    @SerialName("first_post_id") val firstPostId: Long,
    @SerialName("last_post_id") val lastPostId: Long,
    @SerialName("is_locked") val locked: Boolean,
    @SerialName("post_count") val postCount: Long,
    @SerialName("created_at") val createdAt: Instant,
    @SerialName("updated_at") val updatedAt: Instant,
    val title: String,
    @SerialName("user_id") val userId: Long,
    val type: ForumType,
    @SerialName("deleted_at") val deletedAt: Instant? = null,
    val poll: Poll? = null
)

@Serializable
data class Poll(
    @SerialName("allow_vote_change") val canChangeVote: Boolean,
    @SerialName("hide_incomplete_results") val incompleteResultsHidden: Boolean,
    @SerialName("max_votes") val maxVotes: Long,
    val options: List<PollOption>,
    val title: BB,
    @SerialName("started_at") val startedAt: Instant,
    @SerialName("total_vote_count") val totalVotes: Long,
    @SerialName("last_vote_at") val lastVoteAt: Instant? = null,
    @SerialName("ended_at") val endedAt: Instant? = null,
)

@Serializable
data class BB(
    val bbcode: String,
    val html: String,
)

@Serializable
data class PollOption(
    val id: Long,
    val text: BB,
    @SerialName("vote_count") val voteCount: Long? = null,
)
