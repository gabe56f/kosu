package hu.gabe.kosu.objects.api.beatmap

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = LanguageSerializer::class)
enum class BeatmapLanguage(val value: Short) {
    UNSPECIFIED(1),
    ENGLISH(2),
    JAPANESE(3),
    CHINESE(4),
    INSTRUMENTAL(5),
    KOREAN(6),
    FRENCH(7),
    GERMAN(8),
    SWEDISH(9),
    SPANISH(10),
    ITALIAN(11),
    RUSSIAN(12),
    POLISH(13),
    OTHER(14);
}

object LanguageSerializer : KSerializer<BeatmapLanguage> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("hu.gabe.objects.Language", PrimitiveKind.SHORT)

    override fun serialize(encoder: Encoder, value: BeatmapLanguage) {
        encoder.encodeShort(value.value)
    }

    override fun deserialize(decoder: Decoder): BeatmapLanguage {
        val value = decoder.decodeShort()
        return BeatmapLanguage.entries.first { it.value == value }
    }
}