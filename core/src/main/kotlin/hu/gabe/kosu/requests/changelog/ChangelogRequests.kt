package hu.gabe.kosu.requests.changelog

import hu.gabe.kosu.Kosu
import hu.gabe.kosu.extensions.decodeFromJson
import hu.gabe.kosu.extensions.get
import hu.gabe.kosu.objects.api.changelog.Build
import hu.gabe.kosu.objects.api.changelog.ChangelogMessageFormat
import hu.gabe.kosu.objects.api.changelog.ChangelogsResult
import hu.gabe.kosu.requests.BaseRequestV2

class ChangelogRequests(kosu: Kosu) : BaseRequestV2(kosu) {
    fun getBuild(stream: String, build: String): Build =
        client.get("$endpoint/changelog/$stream/$build").decodeFromJson(kosu)

    fun all(
        from: String? = null,
        to: String? = null,
        maxId: Long? = null,
        stream: String? = null,
        messageFormats: List<ChangelogMessageFormat>? = null
    ): ChangelogsResult = client.get(
        "$endpoint/changelog", emptyMap(), listOf(
            "from" to from,
            "to" to to,
            "max_id" to maxId?.toString(),
            "stream" to stream,
            *messageFormats?.map { "message_formats[]" to it.stringLiteral }?.toTypedArray() ?: emptyArray(),
        )
    ).decodeFromJson(kosu)

    operator fun get(buildVersionOrStream: String): Build =
        client.get("$endpoint/changelog/$buildVersionOrStream").decodeFromJson(kosu)

    operator fun get(buildId: Long): Build =
        client.get("$endpoint/changelog/$buildId", emptyMap(), listOf("key" to "id")).decodeFromJson(kosu)
}