package hu.gabe.kosu.objects.api.forum

import kotlinx.serialization.Serializable

@Serializable
data class Forum(
    val id: Long,
    val name: String,
    val description: String,
    val subforums: List<Forum> = emptyList()
)
