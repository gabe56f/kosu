package hu.gabe.kosu.requests.legacy

import hu.gabe.kosu.Kosu

class V1Requests(kosu: Kosu) {

    @get:JvmName("users")
    val users: UserRequests = UserRequests(kosu)

    @get:JvmName("beatmaps")
    val beatmaps: BeatmapRequests = BeatmapRequests(kosu)

    @get:JvmName("scores")
    val scores: ScoreRequests = ScoreRequests(kosu)

}