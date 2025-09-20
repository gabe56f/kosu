package hu.gabe.kosu.requests.news

import hu.gabe.kosu.Kosu
import hu.gabe.kosu.annotations.RequiresOauth
import hu.gabe.kosu.extensions.decodeFromJson
import hu.gabe.kosu.extensions.get
import hu.gabe.kosu.objects.api.news.NewsPost
import hu.gabe.kosu.requests.BaseRequestV2
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class NewsRequests(kosu: Kosu) : BaseRequestV2(kosu) {
    val url = "$endpoint/news/{post?}"

    @RequiresOauth
    fun all(
        limit: Int? = null, year: Int? = null, cursorString: String? = null
    ): AllNewsResponse = client.get(
        url, mapOf("post" to null), listOf(
            "limit" to limit?.toString(), "year" to year?.toString(), "cursor_string" to cursorString
        )
    ).decodeFromJson(kosu)

    @RequiresOauth
    internal fun get(post: Any, type: String? = "id"): NewsPost =
        client.get(url, mapOf("post" to post.toString()), listOf("key" to type)).decodeFromJson(kosu)

    @RequiresOauth
    operator fun get(postId: Long): NewsPost = get(postId, "id")

    @RequiresOauth
    operator fun get(slug: String): NewsPost = get(slug, null)

    @Serializable
    data class AllNewsResponse(
        @SerialName("cursor_string") val cursorString: String,
        @SerialName("news_posts") val posts: List<NewsPost>,
        @SerialName("news_sidebar") val newsSidebar: NewsSidebar
    ) {
        @Serializable
        data class NewsSidebar(
            @SerialName("current_year") val currentYear: Int,
            @SerialName("news_posts") val posts: List<NewsPost>,
            val years: List<Int>
        )
    }
}