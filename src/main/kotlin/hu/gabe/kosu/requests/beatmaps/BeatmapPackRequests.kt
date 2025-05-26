package hu.gabe.kosu.requests.beatmaps

import hu.gabe.kosu.Kosu
import hu.gabe.kosu.annotations.RequiresOauth
import hu.gabe.kosu.extensions.decodeFromJson
import hu.gabe.kosu.extensions.get
import hu.gabe.kosu.objects.KosuChild
import hu.gabe.kosu.objects.api.beatmap.BeatmapPack
import hu.gabe.kosu.requests.BaseRequestV2
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class BeatmapPackRequests(kosu: Kosu) : BaseRequestV2(kosu) {
    val url = "$endpoint/beatmaps/packs/{pack?}"

    @RequiresOauth
    operator fun get(pack: String): BeatmapPack {
        canExecute()
        return client.get(url, mapOf("pack" to pack)).decodeFromJson(kosu)
    }

    @RequiresOauth
    fun all(): List<BeatmapPack> {
        canExecute()
        return client.get(url, mapOf("pack" to null)).decodeFromJson<BeatmapPacks>(kosu).packs
    }

    @Serializable
    data class BeatmapPacks(
        @SerialName("beatmap_packs") val packs: List<BeatmapPack>
    ) : KosuChild()
}