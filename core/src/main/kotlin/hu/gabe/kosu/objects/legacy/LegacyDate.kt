package hu.gabe.kosu.objects.legacy

import kotlinx.datetime.*
import kotlinx.datetime.format.char
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


typealias LegacyInstant = @Serializable(with = LegacyDateSerializer::class) Instant

val MYSQL_DATE = LocalDateTime.Format {
    year()
    char('-')
    monthNumber()
    char('-')
    dayOfMonth()
    char(' ')
    hour()
    char(':')
    minute()
    char(':')
    second()
}


object LegacyDateSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("hu.gabe.objects.legacy.LegacyDate", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(value.toLocalDateTime(TimeZone.UTC).format(MYSQL_DATE))
    }

    override fun deserialize(decoder: Decoder): Instant {
        return MYSQL_DATE.parse(decoder.decodeString()).toInstant(TimeZone.UTC)
    }

}