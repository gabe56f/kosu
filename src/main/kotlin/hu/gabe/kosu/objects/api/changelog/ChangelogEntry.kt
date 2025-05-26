package hu.gabe.kosu.objects.api.changelog

import hu.gabe.kosu.objects.KosuChild
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangelogEntry(
    val category: String,
    val major: Boolean,
    val type: String,
    @SerialName("created_at") val createdAt: Instant? = null,
    @SerialName("github_pull_request_id") val pullRequest: Int? = null,
    @SerialName("github_url") val githubUrl: String? = null,
    val id: Long? = null,
    val repository: String? = null,
    val title: String? = null,
    val url: String? = null,
    @SerialName("github_user") val contributor: GithubUser? = null,
    val message: String? = null,
    @SerialName("message_html") val messageHtml: String? = null,
) : KosuChild()