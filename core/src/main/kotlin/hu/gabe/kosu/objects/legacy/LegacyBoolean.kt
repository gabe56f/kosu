package hu.gabe.kosu.objects.legacy

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

typealias LegacyBoolean = @Serializable(with = LegacyBooleanSerializer::class) Boolean

object LegacyBooleanSerializer : KSerializer<Boolean> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("hu.gabe.objects.legacy.LegacyBoolean", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Boolean) {
        encoder.encodeString(if (value) "1" else "0")
    }

    override fun deserialize(decoder: Decoder): Boolean {
        val value = decoder.decodeString()
        return value == "1" || value == "true"
    }

}