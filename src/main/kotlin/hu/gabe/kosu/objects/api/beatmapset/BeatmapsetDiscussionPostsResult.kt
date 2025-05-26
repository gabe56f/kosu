package hu.gabe.kosu.objects.api.beatmapset

import hu.gabe.kosu.objects.KosuChild
import hu.gabe.kosu.objects.api.users.User
import kotlinx.serialization.SerialName

data class BeatmapsetDiscussionPostsResult(
    val beatmapsets: List<Beatmapset>,
    val posts: List<BeatmapsetDiscussionPost>,
    val users: List<User>,
    @SerialName("cursor_string") val cursorString: String,
) : KosuChild()

enum class SortingOrder(internal val stringLiteral: String) {
    NEWEST_FIRST("id_desc"),
    OLDEST_FIRST("id_asc");
}

enum class BeatmapsetDiscussionPostType(internal val stringLiteral: String) {
    FIRST_POST("first"),
    REPLY("reply"),
    SYSTEM("system");
}
