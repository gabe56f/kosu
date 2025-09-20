package hu.gabe.kosu.objects.api.news

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsPost(
    val id: Long,
    val slug: String,
    val title: String,
    val author: String,
    @SerialName("edit_url") val editUrl: String,
    @SerialName("first_image") val firstImage: String,
    @SerialName("first_image@2x") val firstImageUpscaled: String,
    @SerialName("published_at") val publishedAt: Instant,
    @SerialName("updated_at") val updatedAt: Instant,
)
