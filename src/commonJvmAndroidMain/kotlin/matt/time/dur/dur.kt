@file:JvmName("DurJvmAndroidKt")

package matt.time.dur

import kotlinx.coroutines.delay
import kotlin.time.Duration


@Suppress("NOTHING_TO_INLINE")
actual inline fun sleep(duration: Duration) = Thread.sleep(duration.inWholeMilliseconds)


actual suspend fun multiPlatformSleep(duration: Duration) {
    delay(duration)
}
