package hu.gabe.kosu.objects.api.beatmap

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = GenreSerializer::class)
enum class BeatmapGenre(val value: Short) {
    UNSPECIFIED(1),
    VIDEO_GAME(2),
    ANIME(3),
    ROCK(4),
    POP(5),
    OTHER(6),
    NOVELTY(7),
    HIP_HOP(9),
    ELECTRONIC(10),
    METAL(11),
    CLASSICAL(12),
    FOLK(13),
    JAZZ(14);
}

object GenreSerializer : KSerializer<BeatmapGenre> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("hu.gabe.objects.Genre", PrimitiveKind.SHORT)

    override fun serialize(encoder: Encoder, value: BeatmapGenre) {
        encoder.encodeShort(value.value)
    }

    override fun deserialize(decoder: Decoder): BeatmapGenre {
        val value = decoder.decodeShort()
        return BeatmapGenre.entries.first { it.value == value }
    }
}