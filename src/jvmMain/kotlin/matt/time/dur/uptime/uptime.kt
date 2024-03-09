package matt.time.dur.uptime


import matt.lang.j.RUNTIME_MX
import kotlin.time.Duration.Companion.milliseconds

fun uptime() = RUNTIME_MX.uptime.milliseconds


