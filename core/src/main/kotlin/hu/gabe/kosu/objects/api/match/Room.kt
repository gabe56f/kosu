package hu.gabe.kosu.objects.api.match

import hu.gabe.kosu.annotations.Ignore
import hu.gabe.kosu.annotations.KosuVisitable
import hu.gabe.kosu.child.KosuChild
import hu.gabe.kosu.objects.api.beatmap.Beatmap
import hu.gabe.kosu.objects.api.beatmap.Mods
import hu.gabe.kosu.objects.api.beatmap.Ruleset
import hu.gabe.kosu.objects.api.users.User
import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
@KosuVisitable
data class Room(
    val id: Long,
    @SerialName("user_id") val userId: Long,
    @SerialName("channel_id") val channelId: Long,
    val name: String,
    val category: RoomCategory,
    val type: RoomType,
    @SerialName("participant_count") val participantCount: Int,
    val active: Boolean,
    @SerialName("has_password") val passwordLocked: Boolean,
    @SerialName("queue_mode") val queueMode: RealtimeQueueMode,
    @SerialName("current_user_score") val currentUserScore: UserScoreAggregate? = null,
    @SerialName("difficulty_range") val difficultyRange: DifficultyRange? = null,
    val host: User? = null,
    val playlist: List<PlaylistItem>? = emptyList(),
    @SerialName("playlist_item_stats") val playlistItemStats: PlaylistItemStats? = null,
    @SerialName("recent_participants") val recentParticipants: List<User>? = null,
    val scores: MultiplayerScores? = null,
    @SerialName("current_playlist_item") val playlistItem: PlaylistItem? = null,
    @SerialName("max_attempts") val maxAttempts: Int? = null,
    @SerialName("starts_at") val startsAt: Instant? = null,
    @SerialName("ends_at") val endsAt: Instant? = null
) : KosuChild() {
    @Ignore
    val leaderboard: RoomLeaderboard by lazy {
        kosu.rooms.getLeaderboard(id)
    }
}

@Serializable
data class UserScoreAggregate(
    val accuracy: Float,
    val attempts: Int,
    val completed: Int,
    val pp: Float,
    @SerialName("room_id") val roomId: Long,
    @SerialName("user_id") val userId: Long,
    @SerialName("total_score") val totalScore: Long,
    @SerialName("playlist_item_attempts") val playlistItemAttempts: PlaylistItemAttempts? = null,
    val position: Int? = null,
    val user: User? = null
) : KosuChild() {
    @Ignore
    val realUser: User by lazy { kosu.users[userId] }

    @Ignore
    val room: Room by lazy { kosu.rooms[roomId] }
}

@Serializable
data class PlaylistItemStats(
    @SerialName("count_active") val activeCount: Int,
    @SerialName("count_total") val totalCount: Int,
    @SerialName("ruleset_ids") val rulesets: List<Ruleset>
)

@Serializable
data class DifficultyRange(
    val max: Float,
    val min: Float,
)

@Serializable
data class PlaylistItemAttempts(
    val attempts: Int,
    val id: Long,
)

@Serializable
data class PlaylistItem(
    val id: Long,
    @SerialName("room_id") val roomId: Long,
    @SerialName("beatmap_id") val beatmapId: Long,
    @SerialName("ruleset_id") val ruleset: Ruleset,
    @SerialName("allowed_mods") val allowedMods: Mods,
    @SerialName("required_mods") val requiredMods: Mods,
    val expired: Boolean,
    @SerialName("owner_id") val ownerId: Long,
    @SerialName("playlist_order") val playlistOrder: Int? = null,
    @SerialName("played_at") val playedAt: Instant? = null,
    val beatmap: Beatmap? = null,
)

@Serializable(with = RealtimeQueueModeSerializer::class)
enum class RealtimeQueueMode(internal val stringLiteral: String) {
    HOST_ONLY("host_only"),
    ALL_PLAYERS("all_players"),
    ALL_PLAYERS_ROUND_ROBIN("all_players_round_robin");
}

@Serializable(with = RoomFilterModeSerializer::class)
enum class RoomFilterMode(internal val stringLiteral: String) {
    ENDED("ended"),
    PARTICIPATED("participated"),
    OWNED("owned"),
    ACTIVE("active");
}

@Serializable(with = RoomTypeSerializer::class)
enum class RoomType(internal val stringLiteral: String) {
    PLAYLISTS("playlists"),
    HEAD_TO_HEAD("head_to_head"),
    TEAM_VERSUS("team_versus");
}

@Serializable(with = RoomCategorySerializer::class)
enum class RoomCategory(internal val stringLiteral: String) {
    NORMAL("normal"),
    SPOTLIGHT("spotlight"),
    FEATURED_ARTIST("featured_artist"),
    DAILY_CHALLENGE("daily_challenge");
}

@Serializable(with = RoomSortSerializer::class)
enum class RoomSort(internal val stringLiteral: String) {
    ENDED("ended"),
    CREATED("created");
}

@Serializable
data class RoomLeaderboard(
    val leaderboard: List<UserScoreAggregate>,
    @SerialName("user_score") val userScore: UserScoreAggregate? = null
)

object RealtimeQueueModeSerializer : KSerializer<RealtimeQueueMode> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "hu.gabe.kosu.objects.RealtimeQueueMode",
        PrimitiveKind.STRING
    )

    override fun serialize(
        encoder: Encoder,
        value: RealtimeQueueMode
    ) {
        encoder.encodeString(value.stringLiteral)
    }

    override fun deserialize(decoder: Decoder): RealtimeQueueMode {
        val value = decoder.decodeString()
        return RealtimeQueueMode.entries.first { it.stringLiteral == value }
    }
}

object RoomFilterModeSerializer : KSerializer<RoomFilterMode> {
    override val descriptor = PrimitiveSerialDescriptor("hu.gabe.kosu.objects.RoomFilterMode", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): RoomFilterMode {
        val value = decoder.decodeString()
        return RoomFilterMode.entries.first { it.stringLiteral == value }
    }

    override fun serialize(
        encoder: Encoder,
        value: RoomFilterMode
    ) {
        encoder.encodeString(value.stringLiteral)
    }
}

object RoomTypeSerializer : KSerializer<RoomType> {
    override val descriptor = PrimitiveSerialDescriptor("hu.gabe.kosu.objects.RoomType", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): RoomType {
        val value = decoder.decodeString()
        return RoomType.entries.first { it.stringLiteral == value }
    }

    override fun serialize(
        encoder: Encoder,
        value: RoomType
    ) {
        encoder.encodeString(value.stringLiteral)
    }
}

object RoomCategorySerializer : KSerializer<RoomCategory> {
    override val descriptor = PrimitiveSerialDescriptor("hu.gabe.kosu.objects.RoomCategory", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: RoomCategory
    ) {
        encoder.encodeString(value.stringLiteral)
    }

    override fun deserialize(decoder: Decoder): RoomCategory {
        val value = decoder.decodeString()
        return RoomCategory.entries.first { it.stringLiteral == value }
    }
}

object RoomSortSerializer : KSerializer<RoomSort> {
    override val descriptor = PrimitiveSerialDescriptor("hu.gabe.kosu.objects.RoomSort", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: RoomSort
    ) {
        encoder.encodeString(value.stringLiteral)
    }

    override fun deserialize(decoder: Decoder): RoomSort {
        val value = decoder.decodeString()
        return RoomSort.entries.first { it.stringLiteral == value }
    }

}