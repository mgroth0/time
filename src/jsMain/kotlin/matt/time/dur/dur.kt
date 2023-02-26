package matt.time.dur

import kotlinx.browser.window
import kotlinx.coroutines.await
import matt.lang.anno.SeeURL
import kotlin.js.Promise
import kotlin.time.Duration

actual fun sleep(duration: Duration) {
  error("this is not really possible on JS :( Use multiPlatformSleep in a coroutine")
}

@SeeURL("https://stackoverflow.com/questions/951021/what-is-the-javascript-version-of-sleep")
actual suspend fun multiPlatformSleep(duration: Duration) {

  Promise { resolve: (Unit)->Unit, _: (Throwable)->Unit ->
	window.setTimeout(resolve, duration.inWholeMilliseconds.toInt())
  }.await()
}