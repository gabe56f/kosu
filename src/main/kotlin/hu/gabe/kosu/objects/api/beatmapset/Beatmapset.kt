package hu.gabe.kosu.objects.api.beatmapset

import hu.gabe.kosu.objects.KosuChild
import hu.gabe.kosu.objects.api.beatmap.Beatmap
import hu.gabe.kosu.objects.api.beatmap.BeatmapStatus
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Beatmapset(
    val artist: String,
    @SerialName("artist_unicode") val artistUnicode: String,
    val covers: Covers,
    @SerialName("creator") val mapper: String,
    val id: Long,
    val nsfw: Boolean,
    val offset: Int,
    @SerialName("play_count") val plays: Int,
    @SerialName("preview_url") val previewUrl: String,
    val source: String,
    val status: BeatmapStatus,
    val spotlight: Boolean,
    val title: String,
    @SerialName("title_unicode") val titleUnicode: String,
    @SerialName("user_id") val userId: Long,
    @SerialName("video") val hasVideo: Boolean,

    // optional fields
    @SerialName("favorite_count") val favorites: Int? = null,
    val beatmaps: List<Beatmap>? = emptyList(),
    @SerialName("current_nominations") val nominations: List<Nomination>? = emptyList(),
    // TODO: this is missing a lot, especially those that don't have a type denoted in the docs

    // extended fields
    val availability: Availability? = null,
    val bpm: Float? = null,
    @SerialName("can_be_hyped") val hypeable: Boolean? = null,
    @SerialName("deleted_at") val deletedAt: Instant? = null,
    @SerialName("discussion_locked") val discussionLocked: Boolean? = null,
    val hype: Hype? = null,
    @SerialName("is_scoreable") val scoreable: Boolean? = null,
    @SerialName("nominations_summary") val nominationsSummary: NominationsSummary? = null,
    val ranked: BeatmapStatus? = null,
    @SerialName("ranked_date") val rankedAt: Instant? = null,
    val rating: Float? = null,
    @SerialName("storyboard") val hasStoryboard: Boolean? = null,
    @SerialName("submitted_date") val submittedAt: Instant? = null,
    val tags: String? = null,
) : KosuChild()

@Serializable
data class Covers(
    val cover: String,
    @SerialName("cover@2x") val coverUpscaled: String,
    val card: String,
    @SerialName("card@2x") val cardUpscaled: String,
    val list: String,
    @SerialName("list@2x") val listUpscaled: String,
    val slimcover: String,
    @SerialName("slimcover@2x") val slimcoverUpscaled: String,
)

@Serializable
data class Availability(
    @SerialName("download_disabled") val downloadDisabled: Boolean,
    @SerialName("more_information") val moreInformation: String? = null
)

@Serializable
data class NominationsSummary(
    val current: Int? = null,
    val required: Int? = null,
)

@Serializable
data class Hype(
    val current: Int,
    val required: Int,
)