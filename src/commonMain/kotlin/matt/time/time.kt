package matt.time

import matt.lang.unixTime
import kotlin.jvm.JvmInline
import kotlin.time.Duration

const val MINUTE_MS: Int = 60*1000
const val HOUR_MS: Int = 3600*1000


@JvmInline
value class UnixTime(val duration: Duration = unixTime()): Comparable<UnixTime> {

  override fun compareTo(other: UnixTime) = duration.compareTo(other.duration)
  operator fun minus(other: UnixTime) = duration - other.duration
  operator fun plus(other: UnixTime) = duration + other.duration
  operator fun minus(other: Duration) = UnixTime(duration - other)
  operator fun plus(other: Duration) = UnixTime(duration + other)
}



