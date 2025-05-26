package hu.gabe.kosu.requests.beatmaps

import hu.gabe.kosu.Kosu
import hu.gabe.kosu.annotations.RequiresOauth
import hu.gabe.kosu.extensions.decodeFromJson
import hu.gabe.kosu.extensions.get
import hu.gabe.kosu.objects.api.beatmap.BeatmapStatus
import hu.gabe.kosu.objects.api.beatmapset.BeatmapsetDiscussionPostType
import hu.gabe.kosu.objects.api.beatmapset.BeatmapsetDiscussionPostsResult
import hu.gabe.kosu.objects.api.beatmapset.SortingOrder
import hu.gabe.kosu.objects.api.beatmapset.BeatmapsetDiscussionVoteType
import hu.gabe.kosu.objects.api.beatmapset.BeatmapsetDiscussionVotesResult
import hu.gabe.kosu.objects.api.beatmapset.BeatmapsetDiscussionsResult
import hu.gabe.kosu.objects.api.beatmapset.MessageType
import hu.gabe.kosu.requests.BaseRequestV2

class BeatmapsetDiscussionRequests(kosu: Kosu) : BaseRequestV2(kosu) {

    @RequiresOauth
    fun searchPosts(
        beatmapsetDiscussionId: Long? = null,
        sort: SortingOrder = SortingOrder.NEWEST_FIRST,
        types: List<BeatmapsetDiscussionPostType> = listOf(BeatmapsetDiscussionPostType.REPLY),
        user: Long? = null,
        limit: Int? = null,
        page: Int? = null,
        cursorString: String? = null
    ): BeatmapsetDiscussionPostsResult {
        canExecute()
        return client.get(
            "$endpoint/beatmapsets/discussions/posts", emptyMap(), listOf(
                "beatmapset_discussion_id" to beatmapsetDiscussionId?.toString(),
                "limit" to limit?.toString(),
                "page" to page?.toString(),
                "sort" to sort.stringLiteral,
                *types.map { "types[]" to it.stringLiteral }.toTypedArray(),
                "user" to user?.toString(),
                "cursor_string" to cursorString
            )
        ).decodeFromJson(kosu)
    }

    @RequiresOauth
    fun getVotes(
        beatmapsetDiscussionId: Long? = null,
        sort: SortingOrder = SortingOrder.NEWEST_FIRST,
        user: Long? = null,
        receiverUser: Long? = null,
        voteType: BeatmapsetDiscussionVoteType = BeatmapsetDiscussionVoteType.UPVOTES,
        limit: Int? = null,
        page: Int? = null,
        cursorString: String? = null
    ): BeatmapsetDiscussionVotesResult {
        canExecute()
        return client.get(
            "$endpoint/beatmapsets/discussions/votes", emptyMap(), listOf(
                "beatmapset_discussion_id" to beatmapsetDiscussionId?.toString(),
                "sort" to sort.stringLiteral,
                "user" to user?.toString(),
                "receiver" to receiverUser?.toString(),
                "limit" to limit?.toString(),
                "page" to page?.toString(),
                "cursor_string" to cursorString,
                "score" to voteType.value.toString()
            )
        ).decodeFromJson(kosu)
    }

    @RequiresOauth
    fun searchDiscussions(
        beatmapId: Long? = null,
        beatmapsetId: Long? = null,
        beatmapsetStatus: BeatmapStatus = BeatmapStatus.ALL,
        messageTypes: List<MessageType>? = null,
        onlyUnresolved: Boolean = false,
        limit: Int? = null,
        page: Int? = null,
        sort: SortingOrder = SortingOrder.NEWEST_FIRST,
        user: Long? = null,
        cursorString: String? = null
    ): BeatmapsetDiscussionsResult {
        canExecute()
        return client.get(
            "$endpoint/beatmapsets/discussions", emptyMap(), listOf(
                "beatmap_id" to beatmapId?.toString(),
                "beatmapset_id" to beatmapsetId?.toString(),
                "beatmapset_status" to beatmapsetStatus.stringLiteral,
                *(messageTypes?.map { "message_types[]" to it.stringLiteral }?.toTypedArray() ?: emptyArray()),
                "only_unresolved" to onlyUnresolved.toString(),
                "limit" to limit?.toString(),
                "page" to page?.toString(),
                "sort" to sort.stringLiteral,
                "cursor_string" to cursorString,
                "user" to user?.toString()
            )
        ).decodeFromJson(kosu)
    }

}