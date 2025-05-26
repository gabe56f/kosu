package hu.gabe.kosu.objects.api.beatmap

import hu.gabe.kosu.annotations.Ignore
import hu.gabe.kosu.objects.KosuChild
import hu.gabe.kosu.objects.api.beatmapset.Beatmapset
import hu.gabe.kosu.objects.api.users.User
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Beatmap(
    @SerialName("beatmapset_id") val beatmapsetId: Long,
    @SerialName("difficulty_rating") val difficulty: Float,
    val id: Long,
    val mode: Ruleset,
    val status: BeatmapStatus,
    @SerialName("total_length") val length: Int,
    @SerialName("user_id") val userId: Long,
    val version: String,

    // optional attributes
    val beatmapset: Beatmapset? = null,
    val checksum: String? = null,
    @SerialName("current_user_playcount") val currentUserPlaycount: Int? = null,
    val failtimes: Failtimes? = null,
    @SerialName("max_combo") val maxCombo: Int? = null,
    val owners: List<BeatmapOwner>? = emptyList(),

    // extended attributes
    val accuracy: Float? = null,
    val ar: Float? = null,
    val bpm: Float? = null,
    val convert: Boolean? = null,
    val cs: Float? = null,
    val drain: Float? = null,
    @SerialName("count_circles") val circleCount: Int? = null,
    @SerialName("count_sliders") val sliderCount: Int? = null,
    @SerialName("count_spinners") val spinnerCount: Int? = null,
    @SerialName("playcount") val playCount: Int? = null,
    @SerialName("passcount") val passCount: Int? = null,
    @SerialName("hit_length") val hitLength: Int? = null,
    @SerialName("is_scoreable") val scoreable: Boolean? = null,
    @SerialName("last_updated") val lastUpdated: Instant? = null,
    @SerialName("deleted_at") val deletedAt: Instant? = null,
    val ranked: BeatmapStatus? = null,
    val url: String? = null,
) : KosuChild() {
    @get:Ignore
    val user: User
        get() = kosu.users[userId]
}
