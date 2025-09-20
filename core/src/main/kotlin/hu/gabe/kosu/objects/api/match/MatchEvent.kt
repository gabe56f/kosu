package hu.gabe.kosu.objects.api.match

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
data class MatchEvent(
    val id: Long,
    val detail: MatchEventDetail,
    val timestamp: Instant,
    @SerialName("user_id") val userId: Long? = null,
    val game: MatchGame? = null,
)

@Serializable
data class MatchEventDetail(val type: MatchEventType, val text: String)

@Serializable(with = MatchEventTypeSerializer::class)
enum class MatchEventType(internal val stringLiteral: String) {
    HOST_CHANGED("host-changed"),
    MATCH_CREATED("match-created"),
    MATCH_DISBANDED("match-disbanded"),
    OTHER("other"),
    PLAYER_JOINED("player-joined"),
    PLAYER_LEFT("player-left"),
    PLAYER_KICKED("player-kicked");
}

object MatchEventTypeSerializer : KSerializer<MatchEventType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("hu.gabe.objects.match.MatchEventType", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): MatchEventType {
        val value = decoder.decodeString()
        return MatchEventType.entries.first { it.stringLiteral == value }
    }

    override fun serialize(
        encoder: Encoder,
        value: MatchEventType
    ) {
        encoder.encodeString(value.stringLiteral)
    }
}
