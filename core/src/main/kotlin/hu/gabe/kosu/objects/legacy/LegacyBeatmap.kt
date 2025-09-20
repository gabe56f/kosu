package hu.gabe.kosu.objects.legacy

import hu.gabe.kosu.objects.api.beatmap.BeatmapGenre
import hu.gabe.kosu.objects.api.beatmap.BeatmapLanguage
import hu.gabe.kosu.objects.api.beatmap.BeatmapStatus
import hu.gabe.kosu.objects.api.beatmap.Ruleset
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class LegacyBeatmap(
    @SerialName("approved") val ranked: BeatmapStatus,
    @SerialName("submit_date") val submittedDate: LegacyInstant,
    @SerialName("approved_date") val rankedDate: LegacyInstant,
    @SerialName("last_update") val lastUpdate: LegacyInstant,
    val artist: String,
    @SerialName("beatmap_id") val beatmapId: Long,
    @SerialName("beatmapset_id") val beatmapsetId: Long,
    val bpm: Int,
    val creator: String,
    @SerialName("creator_id") val creatorId: Long,

    @SerialName("difficultyrating") val difficultyRating: Float,
    @SerialName("diff_aim") val aimDifficulty: Float,
    @SerialName("diff_speed") val speedDifficulty: Float,

    @SerialName("diff_size") val circleSize: Float,
    @SerialName("diff_overall") val overallDifficulty: Float,
    @SerialName("diff_drain") val drain: Float,
    @SerialName("hit_length") val firstNoteToLastSeconds: Int,
    val source: String,
    @SerialName("genre_id") val genre: BeatmapGenre,
    @SerialName("language_id") val language: BeatmapLanguage,
    val title: String,
    @SerialName("total_length") val totalSeconds: Int,
    val version: String,
    @SerialName("file_md5") val checksum: String,
    @SerialName("mode") val ruleset: Ruleset,
    @Serializable(with = TagSerializer::class) val tags: List<String>,
    @SerialName("favourite_count") val favourites: Int,
    @SerialName("rating") val rating: Float,
    @SerialName("playcount") val playCount: Int,
    @SerialName("passcount") val passCount: Int,
    @SerialName("count_normal") val circleCount: Int,
    @SerialName("count_slider") val sliderCount: Int,
    @SerialName("count_spinner") val spinnerCount: Int,
    @SerialName("max_combo") val maxCombo: Int,
    val storyboard: LegacyBoolean,
    val video: LegacyBoolean,
    @SerialName("download_unavailable") val downloadUnavailable: LegacyBoolean,
    @SerialName("audio_unavailable") val audioUnavailable: LegacyBoolean
)

object TagSerializer : KSerializer<List<String>> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("hu.gabe.objects.legacy.TagSerializer", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: List<String>
    ) {
        encoder.encodeString(value.joinToString(" "))
    }

    override fun deserialize(decoder: Decoder): List<String> {
        return decoder.decodeString().split(" ")
    }

}
