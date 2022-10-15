package matt.time

import matt.lang.unixTime
import matt.model.convert.Converter
import kotlin.jvm.JvmInline
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit
import kotlin.time.toDuration

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


fun DurationUnit.of(amount: Float) = amount.toDouble().toDuration(this)
fun DurationUnit.of(amount: Double) = amount.toDuration(this)
fun DurationUnit.of(amount: Long) = amount.toDuration(this)
fun DurationUnit.of(amount: Int) = amount.toDuration(this)


object MilliSecondDurationConverter: Converter<Duration, Double> {
  override fun convertToB(a: Duration): Double {
	return a.inWholeMilliseconds.toDouble()
  }

  override fun convertToA(b: Double): Duration {
	return b.milliseconds
  }

}

fun Duration.remMillis(d: Duration): Duration {
  return inWholeMilliseconds.rem(d.inWholeMilliseconds).milliseconds
}

