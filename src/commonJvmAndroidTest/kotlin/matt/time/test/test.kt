package matt.time.test


import matt.test.assertions.JupiterTestAssertions.assertRunsInOneMinute
import matt.time.nowLocal
import kotlin.test.Test

class TimeTests {

    @Test
    fun getNow() =
        assertRunsInOneMinute {
            nowLocal()
        }
}
