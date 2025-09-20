package hu.gabe.kosu.objects.api.users

import hu.gabe.kosu.annotations.KosuVisitable
import hu.gabe.kosu.child.KosuChild
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@KosuVisitable
data class UserStatistics(
    @SerialName("count_100") val count100: Long,
    @SerialName("count_300") val count300: Long,
    @SerialName("count_50") val count50: Long,
    @SerialName("count_miss") val countMiss: Long,
    @SerialName("grade_counts") val gradeCounts: GradeCounts,
    @SerialName("hit_accuracy") val hitAccuracy: Float,
    @SerialName("is_ranked") val ranked: Boolean,
    val level: UserLevel,
    @SerialName("maximum_combo") val maximumCombo: Int,
    @SerialName("play_count") val playCount: Int,
    @SerialName("play_time") val playTime: Long,
    val pp: Float,
    @SerialName("ranked_score") val rankedScore: Long,
    @SerialName("replays_watched_by_others") val replaysWatchedByOthers: Int,
    @SerialName("total_score") val totalScore: Long,
    @SerialName("total_hits") val totalHits: Long,
    @SerialName("country_rank") val countryRank: Int? = null,
    @SerialName("rank_change_since_30_days") val rankChangeSince30Days: Int? = null,
    val user: User? = null
) : KosuChild()

@Serializable
data class UserLevel(
    val current: Int,
    val progress: Float
)

@Serializable
data class GradeCounts(
    val a: Long,
    val s: Long,
    @SerialName("sh") val sHidden: Long,
    val ss: Long,
    @SerialName("ssh") val ssHidden: Long
)