package hu.gabe.kosu.objects.legacy

import hu.gabe.kosu.objects.KosuChild
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LegacyScore(
    @SerialName("score_id") val scoreId: Long,
    val score: Int,
    val username: String? = null,
    val count300: Int,
    val count100: Int,
    val count50: Int,
    @SerialName("countmiss") val countMiss: Int,
    @SerialName("maxcombo") val maxCombo: Int,
    @SerialName("countkatu") val countKatu: Int,
    @SerialName("countgeki") val countGeki: Int,
    val perfect: LegacyBoolean,
    val enabledMods: Long? = null,
    @SerialName("user_id") val userId: Long,
    val date: LegacyInstant,
    // val rank: Rank,
    val pp: Float,
    @SerialName("replay_available") val replayAvailable: LegacyBoolean
) : KosuChild()
