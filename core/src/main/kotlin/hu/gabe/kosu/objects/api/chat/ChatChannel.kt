package hu.gabe.kosu.objects.api.chat

import hu.gabe.kosu.annotations.KosuVisitable
import hu.gabe.kosu.child.KosuChild
import hu.gabe.kosu.objects.api.users.CurrentUserAttributes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@KosuVisitable
data class ChatChannel(
    @SerialName("channel_id") val channelId: Long,
    val name: String,
    val type: ChannelType,
    @SerialName("message_length_limit") val lengthLimit: Int,
    val moderated: Boolean,
    val description: String? = null,
    val icon: String? = null,
    val uuid: String? = null,
    @SerialName("current_user_attributes") val currentUserAttributes: CurrentUserAttributes? = null,
    @SerialName("last_read_id") @Deprecated("use currentUserAttributes.lastReadId") val lastReadId: Long? = null,
    @SerialName("last_message_id") val lastMessageId: Long? = null,
    @SerialName("recent_messages") val recentMessages: List<ChatMessage>? = emptyList(),
    val users: List<Int>? = emptyList(),
) : KosuChild()
