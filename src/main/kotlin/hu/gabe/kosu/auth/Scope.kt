package hu.gabe.kosu.auth

enum class Scope(internal val json: String) {
    READ_CHAT("chat.read"),
    WRITE_CHAT("chat.write"),
    MANAGE_CHAT("chat.write_manage"),
    DELEGATE("delegate"),
    WRITE_FORUM("forum.write"),
    FRIENDS("friends.read"),
    IDENTIFY("identify"),
    PUBLIC("public");

    companion object {
        /**
         * The default scopes required to access most endpoints, that being: [PUBLIC].
         */
        fun default() = listOf(PUBLIC)

        /**
         * All the scopes except [DELEGATE].
         */
        fun all() = entries.filter { it != DELEGATE }
    }
}