package hu.gabe.kosu.objects.api.chat

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = ChannelTypeSerializer::class)
enum class ChannelType(val stringLiteral: String) {
    PUBLIC("PUBLIC"),
    PRIVATE("PRIVATE"),
    MULTIPLAYER("MULTIPLAYER"),
    SPECTATOR("SPECTATOR"),
    @Deprecated("deprecated on api")
    TEMPORARY("TEMPORARY"),
    PRIVATE_MESSAGE("PM"),
    GROUP("GROUP"),
    ANNOUNCE("ANNOUNCE");
}


object ChannelTypeSerializer : KSerializer<ChannelType> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "hu.gabe.objects.chat.ChannelType",
        PrimitiveKind.STRING
    )

    override fun serialize(
        encoder: Encoder,
        value: ChannelType
    ) {
        encoder.encodeString(value.stringLiteral)
    }

    override fun deserialize(decoder: Decoder): ChannelType {
        val value = decoder.decodeString()
        return ChannelType.entries.first { it.stringLiteral == value }
    }

}