package hu.gabe.kosu.objects.api.changelog

import hu.gabe.kosu.objects.KosuChild
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Build(
    @SerialName("created_at") val createdAt: Instant,
    @SerialName("display_version") val displayVersion: String,
    val id: Long,
    val users: Int,
    @SerialName("update_stream") val updateStream: UpdateStream? = null,
    val version: String? = null,
    @SerialName("youtube_id") val youtubeId: String? = null,
    @SerialName("changelog_entries") val changelogEntries: List<ChangelogEntry>? = emptyList(),
    val versions: Versions? = null
) : KosuChild()

@Serializable
data class Versions(val next: Build? = null, val previous: Build? = null)
