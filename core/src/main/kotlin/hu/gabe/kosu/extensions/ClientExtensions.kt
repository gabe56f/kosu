package hu.gabe.kosu.extensions

import hu.gabe.kosu.Kosu
import hu.gabe.kosu.child.IKosuVisitable
import hu.gabe.kosu.child.KosuChild
import hu.gabe.kosu.objects.api.Error
import kotlinx.coroutines.runBlocking
import okhttp3.Call
import okhttp3.FormBody
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

internal fun OkHttpClient.post(
    url: String,
    form: Map<String, String> = emptyMap(),
): Call {
    return newCall(
        Request.Builder()
            .url(url)
            .post(
                FormBody.Builder().apply {
                    var t = this
                    form.forEach { t = t.add(it.key, it.value) }
                    t
                }.build()
            ).build()
    )
}

internal fun OkHttpClient.post(
    url: String,
    json: String,
): Call {
    return newCall(
        Request.Builder()
            .url(url)
            .post(
                json.toRequestBody("application/json".toMediaTypeOrNull())
            ).build()
    )
}


internal fun OkHttpClient.patch(
    url: String,
    json: String,
): Call {
    return newCall(
        Request.Builder()
            .url(url)
            .patch(
                json.toRequestBody("application/json".toMediaTypeOrNull())
            ).build()
    )
}

internal fun OkHttpClient.get(
    url: String,
    path: Map<String, String?> = emptyMap(),
    query: List<Pair<String, String?>> = emptyList()
): Call {
    var newUrl = url
    path.forEach {
        newUrl = if (it.value == null)
            newUrl.replace("/{${it.key}}?", "").replace("{${it.key}?}", "")
        else newUrl.replace("{${it.key}}", it.value!!).replace("{${it.key}?}", it.value!!)
    }
    return newCall(
        Request.Builder()
            .get()
            .url(
                newUrl.toHttpUrl().newBuilder()
                    .apply {
                        query.forEach {
                            if (it.second == null) return@forEach
                            if (it.second!!.isEmpty()) return@forEach
                            addQueryParameter(it.first, it.second)
                        }
                    }.build()
            ).build()
    )
}

internal fun setKosu(obj: Any, kosu: Kosu): Any {
    if (obj is KosuChild)
        obj.kosu = kosu

    if (obj is IKosuVisitable)
        obj.getVisitableChildren().forEach { setKosu(it, kosu) }

    if (obj is List<*>)
        obj.forEach { it?.let { setKosu(it, kosu) } }

    return obj
}

internal inline fun <reified T> Call.decodeFromJson(kosu: Kosu): T {
    val key = request().url.toString() + (request().body?.toString() ?: "")
    return runBlocking {
        kosu.cache.getOrPut(key) {
            val response = execute()
            val body = response.body.string()

            if (!response.isSuccessful) {
                val error = kosu.json.decodeFromString<Error>(body)
                throw IllegalStateException("Call returned ${response.code}. $error")
            }

            return@getOrPut kosu.json.decodeFromString<T>(body).also {
                setKosu(it!!, kosu)
                // println("Executing request (${request().url}) took ${execute}ms, setting attributes took ${attribute}ms.")
            }
        } as T
    }
}