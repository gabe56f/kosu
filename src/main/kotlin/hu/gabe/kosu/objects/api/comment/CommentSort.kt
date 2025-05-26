package hu.gabe.kosu.objects.api.comment

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = CommentSortSerializer::class)
enum class CommentSort(internal val stringLiteral: String) {
    NEW("new"),
    OLD("old"),
    TOP("top");
}

object CommentSortSerializer : KSerializer<CommentSort> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("hu.gabe.objects.comment.CommentSort",
        PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: CommentSort
    ) {
        encoder.encodeString(value.stringLiteral)
    }

    override fun deserialize(decoder: Decoder): CommentSort {
        val value = decoder.decodeString()
        return CommentSort.entries.first { it.stringLiteral == value }
    }

}
