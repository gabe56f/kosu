package hu.gabe.kosu.objects.api.changelog

import hu.gabe.kosu.annotations.KosuVisitable
import hu.gabe.kosu.child.KosuChild
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@KosuVisitable
data class GithubUser(
    @SerialName("display_name") val displayName: String,
    @SerialName("github_url") val githubUrl: String? = null,
    @SerialName("github_username") val githubUsername: String? = null,
    val id: Long? = null,
    @SerialName("osu_username") val osuUsername: String? = null,
    @SerialName("user_id") val userId: Long? = null,
    @SerialName("user_url") val userUrl: String? = null,
) : KosuChild()
