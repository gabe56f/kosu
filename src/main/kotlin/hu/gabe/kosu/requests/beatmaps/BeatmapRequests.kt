package hu.gabe.kosu.requests.beatmaps

import hu.gabe.kosu.Kosu
import hu.gabe.kosu.annotations.RequiresOauth
import hu.gabe.kosu.extensions.decodeFromJson
import hu.gabe.kosu.extensions.get
import hu.gabe.kosu.extensions.post
import hu.gabe.kosu.objects.api.beatmap.Beatmap
import hu.gabe.kosu.objects.api.beatmap.BeatmapDifficultyAttributes
import hu.gabe.kosu.objects.api.event.Event
import hu.gabe.kosu.requests.BaseRequestV2
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class BeatmapRequests(kosu: Kosu) : BaseRequestV2(kosu) {
    val url = "$endpoint/beatmaps"

    @RequiresOauth
    fun lookupById(id: Long): Beatmap = lookup(id = id)

    @RequiresOauth
    fun lookupByFileName(filename: String): Beatmap = lookup(filename = filename)

    @RequiresOauth
    fun lookupByChecksum(checksum: String): Beatmap = lookup(checksum = checksum)

    @RequiresOauth
    fun lookup(id: Long? = null, filename: String? = null, checksum: String? = null): Beatmap {
        canExecute()
        if ((id ?: filename
            ?: checksum) == null
        ) throw IllegalArgumentException("At least one of the parameters must be provided.")

        return client.get(
            "$url/lookup", emptyMap(), listOf(
                "id" to id?.toString(),
                "filename" to filename,
                "checksum" to checksum
            )
        ).decodeFromJson(kosu)
    }

    @RequiresOauth
    operator fun get(ids: List<Int>): List<Beatmap> {
        canExecute()
        return client.get(url, emptyMap(), ids.map {
            "ids[]" to it.toString()
        }).decodeFromJson<Beatmaps>(kosu).beatmaps
    }

    @RequiresOauth
    operator fun get(id: Long): Beatmap {
        canExecute()
        return client.get("$url/$id", emptyMap(), emptyList()).decodeFromJson(kosu)
    }

    @RequiresOauth
    fun getBeatmapAttributes(beatmapId: Long): BeatmapDifficultyAttributes {
        canExecute()
        return client.post("$url/$beatmapId/attributes", emptyMap()).decodeFromJson<BeatmapAttributes>(kosu).attributes
    }

    @RequiresOauth
    // TODO: this isn't right, the returned objects are "events" but with extended beatmapset objects.
    fun getBeatmapsetEvents(): List<Event> = client.get("${url}ets/events").decodeFromJson<BeatmapEvents>(kosu).events

    @Serializable
    // TODO: this isn't right, the returned objects are "events" but with extended beatmapset objects.
    data class BeatmapEvents(val events: List<Event>)

    @Serializable
    data class BeatmapAttributes(@SerialName("Attributes") val attributes: BeatmapDifficultyAttributes)

    @Serializable
    data class Beatmaps(val beatmaps: List<Beatmap>)
}