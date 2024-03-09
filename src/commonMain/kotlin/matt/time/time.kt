package matt.time

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.Month.DECEMBER
import kotlinx.datetime.Month.FEBRUARY
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import matt.lang.common.unsafeErr
import matt.lang.convert.BiConverter
import matt.lang.function.Op
import matt.lang.function.Produce
import matt.lang.unixTime
import matt.model.flowlogic.controlflowstatement.ControlFlow
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

fun currentTime() = nowLocal().time

fun unsafeErrorAfter(
    month: Month,
    day: Int,
    year: Int,
    message: String
) {
    val lastDay = LocalDate(year = year, month = month, dayOfMonth = day)
    val today = nowLocal().date


    if (
        today > lastDay
    ) {
        error(message)
    } else if (today < lastDay) {
        val daysLeft = (lastDay - today).days
        check(daysLeft >= 1)
        if (daysLeft > 10) {
            unsafeErr("Throw an error here in more than $daysLeft days? Are you sure? Seems unstable, and like probably a mistake...")
        }
    }
}

inline fun ifPast(month: Month, day: Int, year: Int, op: Op) {
    if (nowLocal().date > LocalDate(year = year, month = month, dayOfMonth = day)) {
        op()
    }
}

inline fun ifPastFeb2024(op: Op) {
    if (pastFeb2024) {
        op()
    }
}

val pastFeb2024 by lazy {
    val d = nowLocal()
    d.year > 2024 || d.month > FEBRUARY
}


enum class AMOrPM { AM, PM }






val LocalDateTime.minPart
    get() =
        when {
            minute < 10 -> "0$minute"
            else        -> minute
        }
val LocalDateTime.halfDayHour
    get() =
        when (val h = hour % 12) {
            0    -> 12
            else -> h
        }
val LocalDateTime.amOrPm get() = if (hour >= 13) PM else AM
fun UnixTime.toLocalDateTime() = toInstant().toLocalDateTime()
fun Instant.toLocalDateTime() = toLocalDateTime(TimeZone.currentSystemDefault())
fun UnixTime.toInstant() = Instant.fromEpochMilliseconds(duration.inWholeMilliseconds)

val myDateTimeFormat =
    LocalDateTime.Format {
        dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
        char(',')
        char(' ')
        monthName(MonthNames.ENGLISH_ABBREVIATED)
        char(' ')
        dayOfMonth(Padding.NONE)
        char(',')
        char(' ')
        amPmHour(Padding.NONE)
        char(':')
        minute(Padding.ZERO)
        char(' ')
        amPmMarker(am = "AM", pm = "PM")
    }
private val formattedFormat =
    LocalDateTime.Format {
        monthNumber(Padding.NONE)
        char('/')
        dayOfMonth(Padding.NONE)
        char('/')
        year(Padding.NONE)
        char(' ')
        amPmHour(Padding.NONE)
        char(':')
        minute(Padding.ZERO)
        char(' ')
        amPmMarker(am = "AM", pm = "PM")
    }

fun LocalDateTime.formatShort() = format(formattedFormat)




const val MINUTE_MS: Int = 60 * 1000
const val HOUR_MS: Int = 3600 * 1000

fun Duration.toUnixTime() = UnixTime(this)

fun Instant.toUnixTime() = UnixTime(this)


val Number.unixSeconds: UnixTime
    get() = UnixTime(toDouble().seconds)
val Number.unixMS: UnixTime
    get() = UnixTime(toLong().milliseconds)

/*Should I get rid of this and just use Instant?*/
@Serializable
@JvmInline
value class UnixTime(val duration: Duration = unixTime()) : Comparable<UnixTime> {

    constructor(instant: Instant): this(instant.toEpochMilliseconds().milliseconds)


    companion object {
        val EPOCH = UnixTime(Duration.ZERO)
    }

    override fun compareTo(other: UnixTime) = duration.compareTo(other.duration)
    operator fun minus(other: UnixTime) = duration - other.duration
    operator fun plus(other: UnixTime) = duration + other.duration
    operator fun minus(other: Duration) = UnixTime(duration - other)
    operator fun plus(other: Duration) = UnixTime(duration + other)

    fun timeSince() = UnixTime() - this

    override fun toString(): String = "UnixTime(${Instant.fromEpochMilliseconds(duration.inWholeMilliseconds)})"
}

fun timeSince(unixTime: UnixTime) = unixTime.timeSince()


fun DurationUnit.of(amount: Float) = amount.toDouble().toDuration(this)
fun DurationUnit.of(amount: Double) = amount.toDuration(this)
fun DurationUnit.of(amount: Long) = amount.toDuration(this)
fun DurationUnit.of(amount: Int) = amount.toDuration(this)


object MilliSecondDurationConverter : BiConverter<Duration, Double> {
    override fun convertToB(a: Duration): Double = a.inWholeMilliseconds.toDouble()

    override fun convertToA(b: Double): Duration = b.milliseconds
}

fun Duration.remMillis(d: Duration): Duration = inWholeMilliseconds.rem(d.inWholeMilliseconds).milliseconds

val ONE_NANOSECOND = 1.nanoseconds
val ONE_MICROSECOND = 1.microseconds
val ONE_MILLISECOND = 1.milliseconds
val ONE_SECOND = 1.seconds
val ONE_MINUTE = 1.minutes
val ONE_HOUR = 1.hours
val ONE_DAY = 1.days

val Duration.largestFullUnit
    get() =
        when {
            this == Duration.ZERO  -> null
            this > ONE_DAY         -> DurationUnit.DAYS
            this > ONE_HOUR        -> DurationUnit.HOURS
            this > ONE_MINUTE      -> DurationUnit.MINUTES
            this > ONE_SECOND      -> DurationUnit.SECONDS
            this > ONE_MILLISECOND -> DurationUnit.MILLISECONDS
            this > ONE_MICROSECOND -> DurationUnit.MICROSECONDS
            this >= ONE_NANOSECOND -> DurationUnit.NANOSECONDS
            else                   -> error("could not figure out largestFullUnit for $this")
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
        if (op() === ControlFlow.BREAK) break
        sleep(interval)
    }
    return result
}

fun nowLocal() = Clock.System.now().toLocalDateTime()


@Serializable
class MyKey(
    val key: String,
    private val expires: UnixTime
) {
    fun isExpiredNow() = UnixTime() >= expires
    fun expiresIn() = expires - UnixTime()
}

fun Month.previous() =
    when (this) {
        Month.JANUARY ->   DECEMBER
        else -> Month(number - 1)
    }
fun Month.next() =
    when (this) {
        DECEMBER -> Month.JANUARY
        else -> Month(number + 1)
    }

fun LocalDateTime.startOfThisMonth(): LocalDateTime {
    val date =
        LocalDate(
            year =  year,
            month = month,
            dayOfMonth = 1
        )
    return date.atTime(0, 0, 0, 0)
}
fun LocalDateTime.startOfNextMonth(): LocalDateTime {
    val y: Int = if (month == DECEMBER) year + 1 else year
    val m: Month = month.next()
    val d = 1
    val date =
        LocalDate(
            year =  y,
            month = m,
            dayOfMonth = d
        )

    return date.atTime(0, 0, 0, 0)
}

operator fun LocalDateTime.minus(other: LocalDateTime): Duration = toInstant(TimeZone.UTC) - other.toInstant(TimeZone.UTC)
