package app.web.chechkysh.features.app.presentation

import kotlinx.serialization.Serializable

@Serializable
data class Users(
    val email: String,
    val points: Float
)