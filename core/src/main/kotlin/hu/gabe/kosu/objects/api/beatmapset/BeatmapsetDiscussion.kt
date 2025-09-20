package hu.gabe.kosu.objects.api.beatmapset

import hu.gabe.kosu.annotations.KosuVisitable
import hu.gabe.kosu.child.KosuChild
import hu.gabe.kosu.objects.api.beatmap.Beatmap
import hu.gabe.kosu.objects.api.users.CurrentUserAttributes
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
@KosuVisitable
data class BeatmapsetDiscussion(
    val id: Long,
    @SerialName("beatmapset_id") val beatmapsetId: Long,
    @SerialName("can_be_resolved") val canBeResolved: Boolean,
    @SerialName("can_grant_kudosu") val canGrantKudosu: Boolean,
    @SerialName("kudosu_denied") val kudosuDenied: Boolean,
    @SerialName("created_at") val createdAt: Instant,
    @SerialName("updated_at") val updatedAt: Instant,
    @SerialName("last_post_at") val lastPostAt: Instant,
    @SerialName("message_type") val messageType: MessageType,
    val resolved: Boolean,
    val posts: List<BeatmapsetDiscussionPost> = emptyList(),
    @SerialName("current_user_attributes") val currentUserAttributes: CurrentUserAttributes? = null,
    @SerialName("starting_post") val startingPost: BeatmapsetDiscussionPost? = null,
    @SerialName("deleted_at") val deletedAt: Instant? = null,
    @SerialName("deleted_by_id") val deletedById: Long? = null,
    @SerialName("beatmap_id") val beatmapId: Long? = null,
    val beatmap: Beatmap? = null,
    val beatmapset: Beatmapset? = null,
) : KosuChild()

@Serializable(with = MessageTypeSerializer::class)
enum class MessageType(val stringLiteral: String) {
    HYPE("hype"),
    MAPPER_NOTE("mapper_note"),
    PRAISE("praise"),
    PROBLEM("problem"),
    REVIEW("review"),
    SUGGESTION("suggestion");
}

object MessageTypeSerializer : KSerializer<MessageType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("hu.gabe.objects.beatmapset.MessageType", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: MessageType
    ) {
        encoder.encodeString(value.stringLiteral)
    }

    override fun deserialize(decoder: Decoder): MessageType {
        val value = decoder.decodeString()
        return MessageType.entries.first { it.stringLiteral == value }
    }

}