package hu.gabe.kosu.objects.api.beatmap

enum class BeatmapPackType(internal val tag: Char) {
    STD('S'),
    FEATURED('F'),
    TOURNAMENT('P'),
    LOVED('L'),
    CHART('R'),
    THEME('T'),
    ARTIST('A');
}