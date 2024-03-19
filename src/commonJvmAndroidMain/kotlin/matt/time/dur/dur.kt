
package matt.time.dur

import kotlin.time.Duration


@Suppress("NOTHING_TO_INLINE")
actual inline fun sleep(duration: Duration) = Thread.sleep(duration.inWholeMilliseconds)




