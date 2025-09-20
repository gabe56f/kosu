package hu.gabe.kosu.objects.api.beatmap

import kotlinx.serialization.Serializable

@Serializable
data class Failtimes(
    val exit: List<Int> = emptyList(),
    val fail: List<Int> = emptyList()
)