package hu.gabe.kosu.objects.api.score

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = RankSerializer::class)
enum class Rank(internal val stringLiteral: String) {
    SILVER_SS("xh"),
    SILVER_S("sh"),
    SS("x"),
    S("s"),
    A("a"),
    B("b"),
    C("c"),
    D("d"),
    F("f");
}

object RankSerializer : KSerializer<Rank> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "hu.gabe.objects.score.Rank",
        PrimitiveKind.STRING
    )

    override fun serialize(encoder: Encoder, value: Rank) {
        encoder.encodeString(value.stringLiteral)
    }

    override fun deserialize(decoder: Decoder): Rank {
        val value = decoder.decodeString()
        return Rank.entries.first { it.stringLiteral == value }
    }

}