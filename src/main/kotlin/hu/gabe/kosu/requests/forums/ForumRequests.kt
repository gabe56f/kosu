package hu.gabe.kosu.requests.forums

import hu.gabe.kosu.Kosu
import hu.gabe.kosu.annotations.RequiresOauth
import hu.gabe.kosu.auth.Scope
import hu.gabe.kosu.extensions.decodeFromJson
import hu.gabe.kosu.extensions.get
import hu.gabe.kosu.extensions.patch
import hu.gabe.kosu.extensions.post
import hu.gabe.kosu.objects.api.forum.ForumPost
import hu.gabe.kosu.objects.api.forum.ForumTopic
import hu.gabe.kosu.requests.BaseRequestV2
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class ForumRequests(kosu: Kosu) : BaseRequestV2(kosu) {

    @get:JvmName("topics")
    val topics = TopicRequests(kosu)

    @get:JvmName("posts")
    val posts = PostRequests(kosu)

    class PostRequests(kosu: Kosu) : BaseRequestV2(kosu) {
        @RequiresOauth(Scope.PUBLIC, Scope.WRITE_FORUM)
        fun edit(postId: Long, body: String): ForumPost {
            canExecute(Scope.PUBLIC, Scope.WRITE_FORUM)
            return client.patch("$endpoint/forums/posts/$postId", kosu.json.encodeToString(EditPostRequest(body)))
                .decodeFromJson(kosu)
        }
    }

    class TopicRequests(kosu: Kosu) : BaseRequestV2(kosu) {
        @RequiresOauth(Scope.PUBLIC, Scope.WRITE_FORUM)
        operator fun get(topicId: Long): EditableTopic = EditableTopic(kosu, topicId)

        @RequiresOauth(Scope.PUBLIC, Scope.WRITE_FORUM)
        operator fun get(topic: ForumTopic): EditableTopic = EditableTopic(kosu, topic.id)

        @RequiresOauth
        fun all(
            forumId: Long? = null,
            sort: String? = "new",
            limit: Int? = null,
            cursorString: String? = null
        ): TopicsRequest {
            canExecute()
            return client.get(
                "$endpoint/forums/topics", emptyMap(), listOf(
                    "forum_id" to forumId?.toString(),
                    "sort" to sort,
                    "limit" to limit?.toString(),
                    "cursor_string" to cursorString
                )
            ).decodeFromJson(kosu)
        }

        @RequiresOauth(Scope.PUBLIC, Scope.WRITE_FORUM)
        fun newPoll(
            forumId: Long,
            body: String,
            title: String,
            pollHideResults: Boolean,
            pollDurationDays: Int,
            pollOptions: List<String>,
            pollMaxOptions: Int,
            pollTitle: String,
            pollCanChangeVote: Boolean,
        ): CreateTopic {
            canExecute(Scope.PUBLIC, Scope.WRITE_FORUM)
            return client.post(
                "$endpoint/forums/topics", kosu.json.encodeToString(
                    CreateTopicRequest(
                        body = body,
                        title = title,
                        forumId = forumId,
                        withPoll = true,
                        pollHideResults = pollHideResults,
                        pollDurationDays = pollDurationDays,
                        pollOptions = pollOptions.joinToString("\n"),
                        pollMaxOptions = pollMaxOptions,
                        pollTitle = pollTitle,
                        pollVoteChange = pollCanChangeVote
                    )
                )
            ).decodeFromJson(kosu)
        }

        @RequiresOauth(Scope.PUBLIC, Scope.WRITE_FORUM)
        fun new(
            forumId: Long,
            body: String,
            title: String,
        ): CreateTopic {
            canExecute(Scope.PUBLIC, Scope.WRITE_FORUM)
            return client.post(
                "$endpoint/forums/topics",
                kosu.json.encodeToString(CreateTopicRequest(body, forumId, title))
            )
                .decodeFromJson(kosu)
        }

        class EditableTopic(kosu: Kosu, val topicId: Long) : BaseRequestV2(kosu) {
            @RequiresOauth(Scope.PUBLIC, Scope.WRITE_FORUM)
            fun reply(body: String): ForumPost {
                canExecute(Scope.PUBLIC, Scope.WRITE_FORUM)
                return client.post(
                    "$endpoint/forums/topics/$topicId/reply", mapOf(
                        "body" to body
                    )
                ).decodeFromJson(kosu)
            }

            @set:RequiresOauth(Scope.PUBLIC, Scope.WRITE_FORUM)
            var title: String = ""
                set(value) {
                    canExecute(Scope.PUBLIC, Scope.WRITE_FORUM)
                    client.patch(
                        "$endpoint/forums/topics/$topicId", kosu.json.encodeToString(
                            EditTopicRequest(value)
                        )
                    ).execute()
                }
        }
    }

    @Serializable
    data class EditPostRequest(
        val body: String
    )

    @Serializable
    data class EditTopicRequest(
        @SerialName("forum_topic[topic_title]") val title: String,
    )

    @Serializable
    data class CreateTopicRequest(
        val body: String,
        @SerialName("forum_id") val forumId: Long,
        val title: String,
        @SerialName("with_poll") val withPoll: Boolean? = null,
        @SerialName("forum_topic_poll[hide_results]") val pollHideResults: Boolean? = null,
        @SerialName("forum_topic_poll[length_days]") val pollDurationDays: Int? = null,
        @SerialName("forum_topic_poll[max_options]") val pollMaxOptions: Int? = null,
        @SerialName("forum_topic_poll[options]") val pollOptions: String? = null,
        @SerialName("forum_topic_poll[title]") val pollTitle: String? = null,
        @SerialName("forum_topic_poll[vote_change]") val pollVoteChange: Boolean? = null,
    )

    @Serializable
    data class CreateTopic(
        val topic: ForumTopic,
        val post: ForumPost
    )

    @Serializable
    data class TopicsRequest(
        val topics: List<ForumTopic>,
        @SerialName("cursor_string") val cursorString: String? = null
    )

}