package hu.gabe.kosu.objects.api.users

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = PlaystyleSerializer::class)
enum class UserPlaystyle(internal val stringLiteral: String) {
    MOUSE("mouse"),
    KEYBOARD("keyboard"),
    TABLET("tablet"),
    TOUCHSCREEN("touch");
}

object PlaystyleSerializer : KSerializer<UserPlaystyle> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("hu.gabe.objects.users.UserPlaystyle",
        PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: UserPlaystyle
    ) {
        encoder.encodeString(value.stringLiteral)
    }

    override fun deserialize(decoder: Decoder): UserPlaystyle {
        val value = decoder.decodeString()
        return UserPlaystyle.entries.first { it.stringLiteral == value }
    }

}
