package hu.gabe.kosu.objects.api.wiki

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WikiPage(
    @SerialName("available_locales") val availableLocales: List<String>,
    val title: String,
    val layout: String,
    val locale: String,
    val markdown: String,
    val path: String,
    val tags: List<String>,
    val subtitle: String? = null,
)