package hu.gabe.kosu.requests.comments

import hu.gabe.kosu.Kosu
import hu.gabe.kosu.objects.api.comment.CommentSort

class CommentableTypeRequests(private val kosu: Kosu, private val commentableType: String) {

    fun get(
        after: String? = null,
        commentableId: Long? = null,
        parentId: Long? = null,
        sort: CommentSort = CommentSort.NEW
    ) = kosu.comments.get(after, commentableId, commentableType, parentId, sort)

}