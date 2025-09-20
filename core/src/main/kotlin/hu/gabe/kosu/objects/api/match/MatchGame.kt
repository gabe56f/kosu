package hu.gabe.kosu.objects.api.match

import hu.gabe.kosu.objects.api.beatmap.Beatmap
import hu.gabe.kosu.objects.api.beatmap.ModsAcronym
import hu.gabe.kosu.objects.api.beatmap.Ruleset
import hu.gabe.kosu.objects.api.beatmapset.Beatmapset
import hu.gabe.kosu.objects.api.score.Score
import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class MatchGame(
    val id: Long,
    val beatmap: Beatmap,
    @SerialName("beatmap_id") val beatmapId: Long,
    @SerialName("start_time") val startTime: Instant,
    val mode: Ruleset,
    val mods: ModsAcronym,
    val scores: List<Score>,
    @SerialName("scoring_type") val scoringType: MatchScoringType,
    @SerialName("team_type") val teamType: MatchTeamType,
    @SerialName("end_time") val endTime: Instant? = null,
) {
    // included in beatmap / the docs
    val beatmapset: Beatmapset
        get() = beatmap.beatmapset!!
}

@Serializable(with = MatchScoringTypeSerializer::class)
enum class MatchScoringType(internal val stringLiteral: String) {
    SCORE("score"),
    SCORE_V2("scorev2"),
    ACCURACY("accuracy"),
    COMBO("combo");
}

@Serializable(with = MatchTeamTypeSerializer::class)
enum class MatchTeamType(internal val stringLiteral: String) {
    HEAD_TO_HEAD("head-to-head"),
    TEAM_VS_TEAM("team-vs"),
    TAG_COOP("tag-coop"),
    TAG_TEAM_VS("tag-team-vs");
}

object MatchScoringTypeSerializer : KSerializer<MatchScoringType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("hu.gabe.objects.match.MatchScoringType", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: MatchScoringType
    ) {
        encoder.encodeString(value.stringLiteral)
    }

    override fun deserialize(decoder: Decoder): MatchScoringType {
        val value = decoder.decodeString()
        return MatchScoringType.entries.first { it.stringLiteral == value }
    }

}

object MatchTeamTypeSerializer : KSerializer<MatchTeamType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("hu.gabe.objects.match.MatchTeamType", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: MatchTeamType
    ) {
        encoder.encodeString(value.stringLiteral)
    }

    override fun deserialize(decoder: Decoder): MatchTeamType {
        val value = decoder.decodeString()
        return MatchTeamType.entries.first { it.stringLiteral == value }
    }

}
