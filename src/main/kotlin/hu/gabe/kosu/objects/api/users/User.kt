package hu.gabe.kosu.objects.api.users

import hu.gabe.kosu.objects.KosuChild
import hu.gabe.kosu.objects.api.beatmap.Ruleset
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
data class User(
    val id: Long,
    val username: String,
    @SerialName("avatar_url") val avatarUrl: String,
    @SerialName("country_code") val countryCode: String,
    @SerialName("is_active") val active: Boolean,
    @SerialName("is_bot") val bot: Boolean,
    @SerialName("is_deleted") val deleted: Boolean,
    @SerialName("is_online") val online: Boolean,
    @SerialName("is_supporter") val supporter: Boolean,
    @SerialName("pm_friends_only") val friendsOnlyMessage: Boolean,

    // nullable fields
    @SerialName("profile_colour") val profileColour: String? = null,
    @SerialName("last_visit") val lastOnline: Instant? = null,
    @SerialName("default_group") val defaultGroup: String? = null,
    val country: Country? = null,

    // optional fields
    @SerialName("account_history") val accountHistory: List<UserAccountHistory>? = emptyList(),
    @SerialName("active_tournament_banners") val banners: List<ProfileBanner>? = emptyList(),
    val badges: List<UserBadge>? = emptyList(),
    @SerialName("beatmap_playcounts_count") val beatmapPlaycountsCount: Int? = null,
    @SerialName("favourite_beatmapset_count") val favouriteBeatmapsetCount: Int? = null,
    @SerialName("follow_user_mapping") val followUserMapping: List<Int>? = emptyList(),
    @SerialName("follower_count") val followerCount: Int? = null,
    @SerialName("is_restricted") val restricted: Boolean? = null,
    val kudosu: Kudosu? = null,
    // todo: the docs are completely missing this structure???
    // @SerialName("monthly_playcounts") val monthlyPlaycounts: List<UserMonthlyPlaycount> = emptyList(),
    @SerialName("replays_watched_count") val replaysWatched: Int? = null,
    @SerialName("previous_usernames") val previousUsernames: List<String>? = emptyList(),
    @SerialName("scores_best_count") val bestScores: Int? = null,
    @SerialName("scores_first_count") val firstScores: Int? = null,
    @SerialName("scores_recent_count") val recentScores: Int? = null,
    @SerialName("support_level") val supportLevel: Int? = null,
    @SerialName("session_verified") val sessionVerified: Boolean? = null,
    val statistics: UserStatistics? = null,
    @SerialName("rank_highest") val highestRank: HighestRank? = null,

    // mapping fields
    @SerialName("graveyard_beatmapset_count") val graveyardedMaps: Int? = null,
    @SerialName("guest_beatmapset_count") val guestMaps: Int? = null,
    @SerialName("loved_beatmapset_count") val lovedMaps: Int? = null,
    @SerialName("nominated_beatmapset_count") val nominatedMaps: Int? = null,
    @SerialName("pending_beatmapset_count") val pendingMaps: Int? = null,
    @SerialName("ranked_beatmapset_count") val rankedMaps: Int? = null,
    @SerialName("mapping_follower_count") val mappingFollowers: Int? = null,

    // extended fields
    val discord: String? = null,
    @SerialName("has_supported") val supportedBefore: Boolean? = null,
    val interests: String? = null,
    @SerialName("join_date") val joinDate: Instant? = null,
    val location: String? = null,
    val occupation: String? = null,
    val playstyle: List<UserPlaystyle>? = emptyList(),
    val playmode: Ruleset? = null,
    val title: String? = null,
    @SerialName("title_url") val titleUrl: String? = null,
    val twitter: String? = null,
    val website: String? = null,
) : KosuChild()

@Serializable
data class Country(
    val code: String,
    val name: String
)

@Serializable
data class Kudosu(
    val available: Int,
    val total: Int
)

@Serializable
data class ProfileBanner(
    val id: Long,
    @SerialName("tournament_id") val tournamentId: Long,
    val image: String,
    @SerialName("image@2x") val imageUpscaled: String,
)

@Serializable
data class HighestRank(
    val rank: Int,
    @SerialName("updated_at") val updatedAt: Instant
)

@Serializable
data class UserAccountHistory(
    val id: Long,
    @SerialName("length") val lengthSeconds: Int,
    val permanent: Boolean,
    val timestamp: Instant,
    val type: HistoryType,
    val description: String? = null,
)

@Serializable(with = HistoryTypeSerializer::class)
enum class HistoryType(val stringLiteral: String) {
    NOTE("note"),
    RESTRICTION("restriction"),
    SILENCE("silence");
}

object HistoryTypeSerializer : KSerializer<HistoryType> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "hu.gabe.objects.users.HistoryType",
        PrimitiveKind.STRING
    )

    override fun serialize(
        encoder: Encoder,
        value: HistoryType
    ) {
        encoder.encodeString(value.stringLiteral)
    }

    override fun deserialize(decoder: Decoder): HistoryType {
        val value = decoder.decodeString()
        return HistoryType.entries.first { it.stringLiteral == value }
    }
}

@Serializable
data class UserBadge(
    @SerialName("awarded_at") val awardedAt: Instant,
    val description: String,
    @SerialName("image@2x_url") val imageUpscaledUrl: String,
    @SerialName("image_url") val imageUrl: String,
    val url: String
)
