package hu.gabe.kosu.objects.api.changelog

import hu.gabe.kosu.annotations.KosuVisitable
import hu.gabe.kosu.child.KosuChild
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@KosuVisitable
data class UpdateStream(
    val id: Long,
    @SerialName("is_featured") val featured: Boolean,
    val name: String,
    @SerialName("display_name") val displayName: String? = null,
    @SerialName("latest_build") val latestBuild: Build? = null,
    @SerialName("user_count") val userCount: Int? = null,
) : KosuChild()
