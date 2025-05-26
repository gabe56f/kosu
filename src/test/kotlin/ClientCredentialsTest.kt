import hu.gabe.kosu.Kosu
import hu.gabe.kosu.auth.Scope
import hu.gabe.kosu.objects.api.beatmap.Mod
import hu.gabe.kosu.objects.api.beatmapset.BeatmapsetSearch
import hu.gabe.kosu.objects.api.beatmapset.BeatmapsetSearchStatus
import hu.gabe.kosu.requests.SearchRequests
import kotlin.test.*

private lateinit var kosu: Kosu

class ClientCredentialsTest {
    private val CLIENT_ID: Int = System.getProperty("kosu_client_id")?.toInt() ?: error("kosu_client_id is not set")
    private val CLIENT_SECRET: String =
        System.getProperty("kosu_client_secret") ?: error("kosu_client_secret is not set")

    @BeforeTest
    fun setup() {
        if (!::kosu.isInitialized) kosu = Kosu.Builder {
            scope(Scope.PUBLIC)
            // scope(Scope.DELEGATE)
        }.build().login(CLIENT_ID, CLIENT_SECRET)
    }

    @Test
    fun `Scope test`() {
        with(kosu) {
            assertFailsWith(IllegalStateException::class) { legacy.users["gabe56f"] }
            assertFailsWith(IllegalStateException::class) { users.me() }
            assertFailsWith(IllegalStateException::class) { forums.topics[123L].title = "new title" }
        }
    }

    @Test
    fun `Room test`() {
        with(kosu) {
            val allRooms = rooms.all()
            assertTrue { allRooms.isNotEmpty() }

            val room = rooms[1372309]
            assertTrue { room.name == "norbi and gabá" }
            assertTrue { room.host?.username == "gabe56f" }
            assertTrue { room.host?.country?.code == "HU" }
            assertTrue { !room.active }

            val leaderboard = rooms.getLeaderboard(1372309)
            assertTrue("Make sure Client Credentials don't return anything here.") { leaderboard.userScore == null }
            assertTrue { leaderboard.leaderboard.all { it.pp == .0f } }

            assertEquals(leaderboard, room.leaderboard)
        }
    }

    @Test
    fun `News test`() {
        with(kosu) {
            val news = news.all()
            assertTrue { news.posts.isNotEmpty() }
        }
    }

    @Test
    fun `Changelog test`() {
        with(kosu) {
            val changelogs = changelogs.all()
            assertTrue { changelogs.builds.isNotEmpty() }
            assertTrue { changelogs.streams.isNotEmpty() }
        }
    }

    @Test
    fun `Beatmapset Discussion Test`() {
        with(kosu) {
            val discussions = beatmapsets.discussions.searchDiscussions(beatmapsetId = 1283387)
            assertTrue { discussions.discussions.isNotEmpty() }
        }
    }

    @Test
    fun `Score test`() {
        with(kosu) {
            val score = scores[490625999]
            assertEquals(3219026, score.userId, "Check score parsing")
            assertEquals(score.beatmap!!.user.username, "CircleChu", "Check extension function")

            // new scores
            val soloScore = scores.standard[4791231767]
            assertEquals(soloScore.user.username, "liponshow", "Check lazer score parsing")

            // score with mods
            val modScore = scores[4615985219]
            assertTrue("Check mod parsing") { Mod.HIDDEN in modScore.mods }
        }
    }

    @Test
    fun `Beatmapset test`() {
        with(kosu) {
            val beatmapset = beatmapsets[1283387]
            assertEquals(beatmapset.mapper, "-Rik-")
        }
    }

    @Test
    fun `Kosu propagation test`() {
        with(kosu) {
            assertNotNull(packs.all().firstOrNull()?.kosu)
        }
    }

    @Test
    fun `Search test`() {
        with(kosu) {
            val userSearch = search("gabe56f", mode = SearchRequests.SearchMode.USERS)
            assertTrue { userSearch.users!!.data.isNotEmpty() }
            assertTrue { userSearch.users!!.data.any { it.username == "gabe56f" } }
            assertTrue { userSearch.wikiPages == null || userSearch.wikiPages.data.isEmpty() }

            val genericSearch = search["peppy"]
            assertTrue { genericSearch.wikiPages != null && genericSearch.wikiPages.data.any { "peppy" in it.title } }
            assertTrue { genericSearch.users != null && genericSearch.users.data.any { it.id == 2L } }
        }
    }

    @Test
    fun `Beatmap request test`() {
        with(kosu) {
            val beatmapsets = beatmaps[listOf(2665294, 4445719)]

            assertEquals(2, beatmapsets.size)
            assertTrue("Check beatmap query") { beatmapsets.any { it.id == 2665294L } } // KARMANATIONS - top diff
            assertTrue { beatmapsets.any { it.id == 4445719L } } // Sunglow

            assertTrue { beatmapsets.any { it.version == "Reincarnation" } }
            assertTrue { beatmapsets.any { it.beatmapset?.mapper == "kxlman" } }
        }
    }

    @Test
    fun `Beatmapset search test`() {
        with(kosu) {
            val searchResult = beatmapsets[BeatmapsetSearch.Builder {
                query("KARMANATIONS")
                status(BeatmapsetSearchStatus.RANKED)
            }.build()]
            assertNotEquals(0, searchResult.beatmapsets.size, "Check search success")
            assertTrue { searchResult.beatmapsets.any { it.mapper == "-Rik-" } }
        }
    }

    @Test
    fun `Client authentication test`() {
        with(kosu) {
            assertTrue(authenticated)
        }
    }

    @Test
    fun `Fetch user test`() {
        with(kosu) {
            val userById = users[15735151]
            val userByName = users["gabe56f"]
            assertEquals(userById, userByName)

            assertTrue { users.getRecentUserActivity(15735151).isEmpty() }

            val multipleUsers = users[listOf(7562902, 9269034)]
            assertTrue { multipleUsers.any { it.id == 7562902L } }
            assertTrue { multipleUsers.any { it.id == 9269034L } }

            val taikoUser = users.taiko[31148838]
            assertEquals(taikoUser.username, "Sinon_33")
            assertTrue { taikoUser.highestRank?.rank == 1 }
        }
    }

    @Test
    fun `User authentication test`() {
        return
        // TODO: no idea how to go about this

        /**
         * val kosu = Kosu.Builder().build()
         *
         * val (_, authflow) = kosu.login(CLIENT_ID, CLIENT_SECRET, "http://localhost:2345", Scope.IDENTIFY, Scope.PUBLIC)
         * println(authflow.url)
         *
         * val code = "..."
         * authflow.code(code)
         *
         * assertTrue(kosu.authenticated)
         * assertNotNull(kosu.auth.token)
         * assertTrue(kosu.deauth())
         */
    }
}