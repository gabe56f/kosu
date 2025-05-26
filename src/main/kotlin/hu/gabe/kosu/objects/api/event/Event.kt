package hu.gabe.kosu.objects.api.event

import hu.gabe.kosu.objects.KosuChild
import hu.gabe.kosu.objects.api.beatmap.Ruleset
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
data class Event(
    val id: Long,
    val type: EventType,
    @SerialName("created_at") val createdAt: Instant,
    //val achievement: Achievement? = null,
    val user: EventUser? = null,
    val beatmap: EventBeatmapOrSet? = null,
    val beatmapset: EventBeatmapOrSet? = null,
    val count: Int? = null,
    val approval: String? = null,
    val scoreRank: String? = null,
    val rank: Int? = null,
    val mode: Ruleset? = null,
) : KosuChild()

@Serializable
data class EventBeatmapOrSet(
    val title: String,
    val url: String
)

@Serializable
data class EventUser(
    val username: String,
    val url: String,
    val previousUsername: String? = null
)

@Serializable(with = EventTypeSerializer::class)
enum class EventType(internal val stringLiteral: String) {
    ACHIEVEMENT("achievement"),
    BEATMAP_PLAYCOUNT("beatmapPlaycount"),
    BEATMAPSET_APPROVE("beatmapsetApprove"),
    BEATMAPSET_DELETE("beatmapsetDelete"),
    BEATMAPSET_REVIVE("beatmapsetRevive"),
    BEATMAPSET_UPDATE("beatmapsetUpdate"),
    BEATMAPSET_UPLOAD("beatmapsetUpload"),
    RANK("rank"),
    RANK_LOST("rankLost"),
    USER_SUPPORT_AGAIN("userSupportAgain"),
    USER_SUPPORT_FIRST("userSupportFirst"),
    USER_SUPPORT_GIFT("userSupportGift"),
    USERNAME_CHANGE("usernameChange");
}

object EventTypeSerializer : KSerializer<EventType> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("hu.gabe.objects.event.EventType", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: EventType
    ) {
        encoder.encodeString(value.stringLiteral)
    }

    override fun deserialize(decoder: Decoder): EventType {
        val value = decoder.decodeString()
        return EventType.entries.first { it.stringLiteral == value }
    }

}


