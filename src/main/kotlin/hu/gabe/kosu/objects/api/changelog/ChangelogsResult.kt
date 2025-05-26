package hu.gabe.kosu.objects.api.changelog

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangelogsResult(
    val builds: List<Build>,
    val streams: List<UpdateStream>,
    val search: ChangelogSearch
)

enum class ChangelogMessageFormat(val stringLiteral: String) {
    MARKDOWN("markdown"),
    HTML("html");
}

@Serializable
data class ChangelogSearch(
    /**
     * Alwayys `21`.
     */
    val limit: Int = 21,
    val from: String? = null,
    val to: String? = null,
    @SerialName("max_id") val maxId: Long? = null,
    val stream: String? = null
)
