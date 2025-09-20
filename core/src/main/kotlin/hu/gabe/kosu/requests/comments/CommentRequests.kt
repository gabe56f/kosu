package hu.gabe.kosu.requests.comments

import hu.gabe.kosu.Kosu
import hu.gabe.kosu.annotations.RequiresOauth
import hu.gabe.kosu.extensions.decodeFromJson
import hu.gabe.kosu.extensions.get
import hu.gabe.kosu.objects.api.comment.CommentBundle
import hu.gabe.kosu.objects.api.comment.CommentSort
import hu.gabe.kosu.requests.BaseRequestV2

class CommentRequests(kosu: Kosu) : BaseRequestV2(kosu) {
    @get:JvmName("build")
    val build = CommentableTypeRequests(kosu, "build")

    @get:JvmName("beatmapset")
    val beatmapset = CommentableTypeRequests(kosu, "beatmapset")

    @get:JvmName("news")
    val news = CommentableTypeRequests(kosu, "news_post")

    @RequiresOauth
    fun get(
        after: String? = null,
        commentableId: Long? = null,
        commentableType: String? = null,
        parentId: Long? = null,
        sort: CommentSort = CommentSort.NEW,
    ): CommentBundle = client.get(
        "$endpoint/comments", emptyMap(), listOf(
            "after" to after,
            "commentable_id" to commentableId?.toString(),
            "commentable_type" to commentableType,
            "parent_id" to parentId?.toString(),
            "sort" to sort.stringLiteral
        )
    ).decodeFromJson(kosu)

    operator fun get(comment: String): CommentBundle = client.get("$endpoint/comments/$comment").decodeFromJson(kosu)
}