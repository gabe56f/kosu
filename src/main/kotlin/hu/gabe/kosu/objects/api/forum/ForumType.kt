package hu.gabe.kosu.objects.api.forum

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = ForumTypeSerializer::class)
enum class ForumType(internal val stringLiteral: String) {
    NORMAL("normal"),
    STICKY("sticky"),
    ANNOUNCEMENT("announcement");
}

object ForumTypeSerializer : KSerializer<ForumType> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("hu.gabe.objects.forum.ForumType", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: ForumType
    ) {
        encoder.encodeString(value.stringLiteral)
    }

    override fun deserialize(decoder: Decoder): ForumType {
        val value = decoder.decodeString()
        return ForumType.entries.first { it.stringLiteral == value }
    }

}