package hu.gabe.kosu.objects.api

import kotlinx.serialization.Serializable

@Serializable
data class Error(
    val error: String,
)
