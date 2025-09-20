package hu.gabe.kosu.objects.api.beatmap

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = StatusSerializer::class)
enum class BeatmapStatus(internal val id: Int, internal val stringLiteral: String) {
    GRAVEYARD(-2, "graveyard"),
    WIP(-1, "wip"),
    PENDING(0, "pending"),
    RANKED(1, "ranked"),
    APPROVED(2, "approved"),
    QUALIFIED(3, "qualified"),
    LOVED(4, "loved"),
    ALL(999, "all");
}

object StatusSerializer : KSerializer<BeatmapStatus> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "hu.gabe.objects.beatmaps.BeatmapStatus",
        PrimitiveKind.INT
    )

    override fun serialize(
        encoder: Encoder,
        value: BeatmapStatus
    ) {
        encoder.encodeInt(value.id)
    }

    override fun deserialize(decoder: Decoder): BeatmapStatus {
        val value = decoder.decodeString()
        try {
            val id = value.toInt()
            return BeatmapStatus.entries.first { it.id == id }
        } catch (_: NumberFormatException) {
            return BeatmapStatus.entries.first { it.stringLiteral == value }
        }
    }

}
