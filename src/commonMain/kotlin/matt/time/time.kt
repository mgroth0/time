package matt.time

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import matt.lang.function.Produce
import matt.lang.unixTime
import matt.model.flowlogic.controlflowstatement.ControlFlow
import matt.model.op.convert.Converter
import matt.time.AMOrPM.AM
import matt.time.AMOrPM.PM
import matt.time.dur.sleep
import kotlin.jvm.JvmInline
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.microseconds
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.toDuration

enum class AMOrPM { AM, PM }


//fun localDateTimeNow() = System.now().toLocalDateTime(TimeZone.currentSystemDefault())


val LocalDateTime.minPart
    get() = when {
        minute < 10 -> "0$minute"
        else        -> minute
    }
val LocalDateTime.halfDayHour
    get() = when (val h = hour % 12) {
        0    -> 12
        else -> h
    }
val LocalDateTime.amOrPm get() = if (hour >= 13) PM else AM
fun UnixTime.toLocalDateTime() = toInstant().toLocalDateTime()
fun Instant.toLocalDateTime() = toLocalDateTime(TimeZone.currentSystemDefault())
fun UnixTime.toInstant() = Instant.fromEpochMilliseconds(duration.inWholeMilliseconds)

fun LocalDateTime.formatted() = "${monthNumber}/${dayOfMonth}/${year} ${halfDayHour}:${minPart} ${amOrPm}"

const val MINUTE_MS: Int = 60 * 1000
const val HOUR_MS: Int = 3600 * 1000

fun Duration.toUnixTime() = UnixTime(this)


@Serializable
@JvmInline
value class UnixTime(val duration: Duration = unixTime()) : Comparable<UnixTime> {

    companion object {
        val EPOCH = UnixTime(Duration.ZERO)
    }

    override fun compareTo(other: UnixTime) = duration.compareTo(other.duration)
    operator fun minus(other: UnixTime) = duration - other.duration
    operator fun plus(other: UnixTime) = duration + other.duration
    operator fun minus(other: Duration) = UnixTime(duration - other)
    operator fun plus(other: Duration) = UnixTime(duration + other)

    fun timeSince() = UnixTime() - this
}

fun timeSince(unixTime: UnixTime) = unixTime.timeSince()


fun DurationUnit.of(amount: Float) = amount.toDouble().toDuration(this)
fun DurationUnit.of(amount: Double) = amount.toDuration(this)
fun DurationUnit.of(amount: Long) = amount.toDuration(this)
fun DurationUnit.of(amount: Int) = amount.toDuration(this)


object MilliSecondDurationConverter : Converter<Duration, Double> {
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

val ONE_NANOSECOND = 1.nanoseconds
val ONE_MICROSECOND = 1.microseconds
val ONE_MILLISECOND = 1.milliseconds
val ONE_SECOND = 1.seconds
val ONE_MINUTE = 1.minutes
val ONE_HOUR = 1.hours
val ONE_DAY = 1.days

val Duration.largestFullUnit
    get() = when {
        this == Duration.ZERO  -> null
        this > ONE_DAY         -> DurationUnit.DAYS
        this > ONE_HOUR        -> DurationUnit.HOURS
        this > ONE_MINUTE      -> DurationUnit.MINUTES
        this > ONE_SECOND      -> DurationUnit.SECONDS
        this > ONE_MILLISECOND -> DurationUnit.MILLISECONDS
        this > ONE_MICROSECOND -> DurationUnit.MICROSECONDS
        this >= ONE_NANOSECOND -> DurationUnit.NANOSECONDS
        else                   -> error("could not figure out largestFullUnit for ${this}")
    }


enum class TimeoutLoopResult { TIMEOUT, BROKE }
enum class StartWith { INTERVAL, OP }

fun timeoutLoop(
    interval: Duration,
    startWith: StartWith,
    timeout: Duration,
    op: Produce<ControlFlow>
): TimeoutLoopResult {
    val timeoutEnd = UnixTime() + timeout
    var result = TimeoutLoopResult.BROKE
    if (startWith == StartWith.INTERVAL) sleep(interval)
    while (true) {
        if (UnixTime() > timeoutEnd) {
            result = TimeoutLoopResult.TIMEOUT
            break
        }
        if (op() == ControlFlow.BREAK) break
        sleep(interval)
    }
    return result
}

expect fun nowKotlinDateTime(): LocalDateTime