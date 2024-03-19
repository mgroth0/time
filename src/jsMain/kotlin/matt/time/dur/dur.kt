package matt.time.dur

import kotlin.time.Duration

actual fun sleep(duration: Duration) {

    error("this is not really possible on JS :( Use multiPlatformSleep in a coroutine")
}




