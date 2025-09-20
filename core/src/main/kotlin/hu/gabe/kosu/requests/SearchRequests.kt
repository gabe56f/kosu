package hu.gabe.kosu.requests

import hu.gabe.kosu.Kosu
import hu.gabe.kosu.annotations.RequiresOauth
import hu.gabe.kosu.extensions.decodeFromJson
import hu.gabe.kosu.extensions.get
import hu.gabe.kosu.objects.api.users.User
import hu.gabe.kosu.objects.api.wiki.WikiPage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class SearchRequests(kosu: Kosu) : BaseRequestV2(kosu) {
    @JvmName("search")
    @RequiresOauth
    operator fun invoke(query: String? = null, mode: SearchMode? = null, page: Int? = null): SearchResults = client.get(
        "$endpoint/search", emptyMap(), listOf(
            "mode" to mode?.stringLiteral, "query" to query, "page" to page?.toString()
        )
    ).decodeFromJson(kosu)

    @JvmName("search")
    @RequiresOauth
    operator fun get(query: String): SearchResults = this(query)

    enum class SearchMode(internal val stringLiteral: String) {
        ALL("all"), USERS("user"), WIKI_PAGES("wiki_page");
    }

    @Serializable
    data class SearchResults(
        @SerialName("user") val users: SearchResult<User>? = null,
        @SerialName("wiki_page") val wikiPages: SearchResult<WikiPage>? = null
    )

    @Serializable
    data class SearchResult<T>(
        val data: List<T>,
        val total: Int,
    )
}