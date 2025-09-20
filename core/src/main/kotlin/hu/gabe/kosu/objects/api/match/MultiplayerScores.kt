package hu.gabe.kosu.objects.api.match

import hu.gabe.kosu.objects.api.score.Score
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class MultiplayerScores(
    @SerialName("cursor_string") val cursorString: String,
    val scores: List<Score>,
    val total: Int? = null,
    @SerialName("user_score") val userScore: Score? = null
)

@Serializable
data class MultiplayerScoresAround(
    val higher: MultiplayerScores,
    val lower: MultiplayerScores
)

@Serializable
data class MultiplayerScoresCursor(
    @SerialName("score_id") val scoreId: Long,
    @SerialName("total_score") val totalScore: Int
)

@Serializable(with = MultiplayerScoresSortSerializer::class)
enum class MultiplayerScoresSort(internal val stringLiteral: String) {
    ASCENDING("score_asc"),
    DESCENDING("score_desc");
}

object MultiplayerScoresSortSerializer : KSerializer<MultiplayerScoresSort> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "hu.gabe.objects.match.MultiplayerScoresSort",
        PrimitiveKind.STRING
    )

    override fun serialize(
        encoder: Encoder,
        value: MultiplayerScoresSort
    ) {
        encoder.encodeString(value.stringLiteral)
    }

    override fun deserialize(decoder: Decoder): MultiplayerScoresSort {
        val value = decoder.decodeString()
        return MultiplayerScoresSort.entries.first { it.stringLiteral == value }
    }

}
