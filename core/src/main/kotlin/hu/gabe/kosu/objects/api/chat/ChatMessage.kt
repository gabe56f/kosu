package hu.gabe.kosu.objects.api.chat

import hu.gabe.kosu.annotations.KosuVisitable
import hu.gabe.kosu.child.KosuChild
import hu.gabe.kosu.objects.api.users.User
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@KosuVisitable
data class ChatMessage(
    @SerialName("channel_id") val channelId: Long,
    val content: String,
    @SerialName("is_action") val isAction: Boolean = false,
    @SerialName("message_id") val messageId: Long,
    @SerialName("sender_id") val senderId: Long,
    val timestamp: Instant,
    val type: ChatMessageType,
    val uuid: String? = null,
    val sender: User? = null,
) : KosuChild()
