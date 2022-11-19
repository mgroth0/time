package matt.time.dur

import kotlinx.browser.window
import kotlin.js.Promise
import kotlin.time.Duration

/*https://stackoverflow.com/questions/951021/what-is-the-javascript-version-of-sleep*/
actual fun sleep(duration: Duration) {
  Promise<Unit> { resolve: (Unit)->Unit, _: (Throwable)->Unit ->
	window.setTimeout(resolve, duration.inWholeMilliseconds.toInt())
  }
}