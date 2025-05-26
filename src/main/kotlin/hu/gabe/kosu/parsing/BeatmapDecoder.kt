package hu.gabe.kosu.parsing

import hu.gabe.kosu.objects.game.BeatmapMetadata
import kotlin.reflect.KClass

internal sealed interface BeatmapElement {
    data class Section(val sectionName: String) : BeatmapElement
    data class Data(val data: String) : BeatmapElement
    data class FileVersion(val version: Int) : BeatmapElement
    data class Comment(val comment: String) : BeatmapElement
    class Break : BeatmapElement
    class Empty : BeatmapElement
}

internal val NEW_LINE = """\r?\n""".toRegex()

internal fun String.parseAsBeatmap(): Iterator<BeatmapElement> = split(NEW_LINE).parseAsBeatmap()

internal fun Collection<String>.parseAsBeatmap(): Iterator<BeatmapElement> = iterator {
    var foundNonEmptyLine = false
    var lastSection = ""
    for (line in this@parseAsBeatmap) {
        var line = line
        if (!foundNonEmptyLine) {
            // For whatever reason, map files can start without a file version header
            foundNonEmptyLine = true

            if (line.isBlank())
                try {
                    yield(BeatmapElement.FileVersion(line.trim().split("v")[1].toInt()))
                } catch (_: NumberFormatException) {
                }
            continue
        }

        if (line.startsWith("//")) {
            yield(BeatmapElement.Comment(line.trim()))
            continue
        }

        if (line.isBlank()) {
            yield(BeatmapElement.Empty())
            continue
        }

        if (lastSection != "metadata") {
            val index = line.indexOf("//")
            line = if (index > 0) line.substring(0, index) else line
        }

        if (line.startsWith('[') && line.endsWith(']')) {
            val line = line.substring(1, line.length - 1)
            lastSection = line
            yield(BeatmapElement.Section(line))
            continue
        }

        try {
            //val data = line.parseBeatmapSectionData()
            //yield(data)
        } catch(_: Exception) {
            yield(BeatmapElement.Empty())
        }
    }
}