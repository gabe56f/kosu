package hu.gabe.kosu.objects.api.beatmap

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = RulesetSerializer::class)
enum class Ruleset(internal val stringLiteral: String, internal val id: Int) {
    STANDARD("osu", 0),
    MANIA("mania", 3),
    TAIKO("taiko", 1),
    CTB("fruits", 2);
}

object RulesetSerializer : KSerializer<Ruleset> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "hu.gabe.objects.Ruleset",
        PrimitiveKind.STRING
    )

    override fun serialize(
        encoder: Encoder,
        value: Ruleset
    ) {
        encoder.encodeString(value.stringLiteral)
    }

    override fun deserialize(decoder: Decoder): Ruleset {
        val value = decoder.decodeString()
        return try {
            val id = value.toInt()
            Ruleset.entries.first { it.id == id }
        } catch (_: NumberFormatException) {
            Ruleset.entries.first { it.stringLiteral == value }
        }
    }

}