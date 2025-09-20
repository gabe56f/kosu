package hu.gabe.kosu.objects.api.beatmap

import hu.gabe.kosu.annotations.KosuVisitable
import hu.gabe.kosu.child.KosuChild
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@KosuVisitable
data class BeatmapDifficultyAttributes(
    @SerialName("star_rating") val starRating: Float,
    @SerialName("max_combo") val maxCombo: Int,
    @SerialName("aim_difficulty") val aimDifficulty: Float,
    @SerialName("aim_difficulty_slider_count") val sliderAimDifficulty: Float,
    @SerialName("speed_difficulty") val speedDifficulty: Float,
    @SerialName("speed_note_count") val speedNoteCount: Float,
    @SerialName("slider_factor") val sliderFactor: Float,
    @SerialName("aim_difficult_strain_count") val aimStrainCount: Float,
    @SerialName("speed_difficult_strain_count") val speedStrainCount: Float,
    @SerialName("mono_stamina_factor") val monoStaminaFactor: Float,
) : KosuChild() {
    fun asStdAttributes(): StdBeatmapDifficultyAttributes = StdBeatmapDifficultyAttributes(
        starRating,
        maxCombo,
        aimDifficulty,
        sliderAimDifficulty,
        speedDifficulty,
        speedNoteCount,
        sliderFactor,
        aimStrainCount,
        speedStrainCount
    ).also { it.kosu = kosu }

    fun asTaikoAttributes(): TaikoBeatmapDifficultyAttributes = TaikoBeatmapDifficultyAttributes(
        starRating, maxCombo, monoStaminaFactor
    ).also { it.kosu = kosu }
}

@Serializable
@KosuVisitable
data class TaikoBeatmapDifficultyAttributes(
    val starRating: Float,
    val maxCombo: Int,
    val monoStaminaFactor: Float,
) : KosuChild()

@Serializable
@KosuVisitable
data class StdBeatmapDifficultyAttributes(
    val starRating: Float,
    val maxCombo: Int,
    val aimDifficulty: Float,
    val aimDifficultySliderCount: Float,
    val speedDifficulty: Float,
    val speedNoteCount: Float,
    val sliderFactor: Float,
    val aimStrainCount: Float,
    val speedStrainCount: Float,
) : KosuChild()
