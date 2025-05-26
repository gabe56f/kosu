package hu.gabe.kosu.objects.legacy

import hu.gabe.kosu.objects.KosuChild
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LegacyUser(
    @SerialName("user_id") val id: Long,
    val username: String,
    @SerialName("join_date") val joinDate: LegacyInstant,
    val count300: Int,
    val count100: Int,
    val count50: Int,
    @SerialName("playcount") val playCount: Int,
    @SerialName("ranked_score") val rankedScore: Long,
    @SerialName("total_score") val totalScore: Long,
    @SerialName("pp_rank") val rank: Int,
    @SerialName("level") val level: Float,
    @SerialName("pp_raw") val ppRaw: Float,
    val accuracy: Float,
    @SerialName("count_rank_ss") val ssRanks: Int,
    @SerialName("count_rank_ssh") val sshRanks: Int,
    @SerialName("count_rank_s") val sRanks: Int,
    @SerialName("count_rank_sh") val shRanks: Int,
    @SerialName("count_rank_a") val aRanks: Int,
    @SerialName("country") val country: String,
    @SerialName("total_seconds_played") val totalSecondsPlayed: Long,
    @SerialName("pp_country_rank") val countryRank: Int,
    // val events: List<LegacyEvent>
) : KosuChild()
