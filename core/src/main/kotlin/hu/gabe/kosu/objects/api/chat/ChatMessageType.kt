package hu.gabe.kosu.objects.api.chat

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = ChatMessageTypeSerializer::class)
enum class ChatMessageType(val stringLiteral: String) {
    ACTION("action"),
    MARKDOWN("markdown"),
    PLAIN("plain");
}

object ChatMessageTypeSerializer : KSerializer<ChatMessageType> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "hu.gabe.objects.chat.ChatMessageType",
        PrimitiveKind.STRING
    )

    override fun serialize(
        encoder: Encoder,
        value: ChatMessageType
    ) {
        encoder.encodeString(value.stringLiteral)
    }

    override fun deserialize(decoder: Decoder): ChatMessageType {
        val value = decoder.decodeString()
        return ChatMessageType.entries.first { it.stringLiteral == value }
    }

}