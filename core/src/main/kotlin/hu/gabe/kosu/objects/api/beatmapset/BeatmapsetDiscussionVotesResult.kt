package hu.gabe.kosu.objects.api.beatmapset

import hu.gabe.kosu.objects.api.users.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BeatmapsetDiscussionVotesResult(
    @SerialName("cursor_string") val cursorString: String,
    val discussions: List<BeatmapsetDiscussion> = emptyList(),
    val votes: List<BeatmapsetDiscussionVote> = emptyList(),
    val users: User
)

enum class BeatmapsetDiscussionVoteType(internal val value: Int) {
    UPVOTES(1),
    DOWNVOTES(-1);
}
