package matt.time.test


import matt.test.JupiterTestAssertions.assertRunsInOneMinute
import matt.time.nowKotlinDateTime
import kotlin.test.Test

class TimeTests {

    @Test
    fun getNow() = assertRunsInOneMinute {
        nowKotlinDateTime()
    }
}