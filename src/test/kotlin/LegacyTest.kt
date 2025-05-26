import hu.gabe.kosu.Kosu
import hu.gabe.kosu.objects.api.beatmap.Ruleset
import kotlin.test.*


private lateinit var kosu: Kosu

class LegacyTest {
    private val CLIENT_API_KEY: String =
        System.getProperty("kosu_client_api_key") ?: error("kosu_client_api_key is not set")

    @BeforeTest
    fun setup() {
        if (!::kosu.isInitialized) kosu = Kosu.Builder().build().authenticate(CLIENT_API_KEY)
    }

    @Test
    fun `V2 test`() {
        with(kosu) {
            assertFailsWith(IllegalStateException::class) { users["gabe56f"] }
        }
    }

    @Test
    fun `Beatmap test`() {
        with(kosu) {
            val beatmap = legacy.beatmaps[2665294]
            assertEquals(beatmap.version, "Reincarnation")
        }
    }

    @Test
    fun `User test`() {
        with(kosu) {
            val user = legacy.users["gabe56f"]
            assertEquals(user.id, 15735151L)
            assertEquals(user.username, "gabe56f")
            assertEquals(user.country, "HU")
        }
    }

    @Test
    fun `Score test`() {
        with(kosu) {
            val score = legacy.scores.getUserBest("gabe56f", mode = Ruleset.STANDARD, limit = 20)
            assertTrue { score.any { it.pp > 200 } }
        }
    }

}