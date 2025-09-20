package hu.gabe.kosu.objects.api.beatmapset

import hu.gabe.kosu.objects.api.beatmap.Beatmap
import hu.gabe.kosu.objects.api.users.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BeatmapsetDiscussionsResult(
    val beatmaps: List<Beatmap>,
    val discussions: List<BeatmapsetDiscussion>,
    val users: List<User>,
    @SerialName("included_discussions") val additionalDiscussions: List<BeatmapsetDiscussion>,
    @SerialName("cursor_string") val cursorString: String,
)
