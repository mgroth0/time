package matt.time.dur.common

import matt.lang.common.NEVER
import matt.lang.common.NOT_IMPLEMENTED
import matt.time.dur.sleep
import kotlin.math.floor
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.microseconds
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.DurationUnit.DAYS
import kotlin.time.DurationUnit.HOURS
import kotlin.time.DurationUnit.MICROSECONDS
import kotlin.time.DurationUnit.MILLISECONDS
import kotlin.time.DurationUnit.MINUTES
import kotlin.time.DurationUnit.NANOSECONDS
import kotlin.time.DurationUnit.SECONDS

fun sleepForever(print: Boolean = false) {
    if (print) println("Sleeping Forever")
    sleep(Duration.INFINITE)
}

fun Duration.formatForSpeechSecs() = "$inWholeSeconds seconds"
fun Duration.formatForSpeechMins() = "$inWholeMinutes minutes"
fun Duration.formatForSpeech() =
    when {
        this == 1.nanoseconds  -> "1 nanosecond"
        this == 1.microseconds -> "1 microsecond"
        this < 2.microseconds  -> "$inWholeNanoseconds nanoseconds"
        this == 1.milliseconds -> "1 millisecond"
        this < 2.milliseconds  -> "$inWholeMicroseconds microseconds"
        this == 1.seconds      -> "1 second"
        this < 2.seconds       -> "$inWholeMilliseconds milliseconds"
        this == 1.minutes      -> "1 minute"
        this < 1.minutes       -> "$inWholeMinutes minutes"
        this < 2.minutes       -> "$minutesPart minute, $secondsPart seconds"
        this < 1.hours         -> "$minutesPart minutes, $secondsPart seconds"
        this == 1.hours        -> "1 hour"
        this < 2.hours         -> "$hoursPart hour, $minutesPart minutes, $secondsPart seconds"
        this < 24.hours        -> "$hoursPart hours, $minutesPart minutes, $secondsPart seconds"
        else                   -> "$this"
    }

val Duration.hoursPart: Long
    get() =
        when {
            this < 1.hours  -> 0
            this == 1.hours -> 1
            else            -> floor((inWholeHours % 24.0)).toLong()
        }
val Duration.minutesPart: Long
    get() =
        when {
            this < 1.minutes  -> 0
            this == 1.minutes -> 1
            else              -> floor((inWholeMinutes % 60.0)).toLong()
        }
val Duration.secondsPart: Long
    get() =
        when {
            this < 1.seconds  -> 0
            this == 1.seconds -> 1
            else              -> floor((inWholeSeconds % 60.0)).toLong()
        }
val Duration.millisecondsPart: Long
    get() =
        when {
            this < 1.milliseconds  -> 0
            this == 1.milliseconds -> 1
            else                   -> floor((inWholeMilliseconds % 1000.0)).toLong()
        }
val Duration.isZero get() = this == Duration.ZERO
val Duration.isNotZero get() = !isZero
fun Duration.largestUnitAtLeastOne(): DurationUnit {


    require(!isNegative())
    return when {
        inWholeDays >= 1         -> TODO("get largest unit at least one for $this")
        inWholeHours >= 1        -> HOURS
        inWholeMinutes >= 1      -> MINUTES
        inWholeSeconds >= 1      -> SECONDS
        inWholeMilliseconds >= 1 -> MILLISECONDS
        inWholeMicroseconds >= 1 -> MICROSECONDS
        else                     -> {
            check(inWholeNanoseconds >= 1)
            NANOSECONDS
        }
    }
}

val DurationUnit.unitLabelTwoCharMax
    get() =
        when (this) {
            NANOSECONDS  -> "ns"
            MICROSECONDS -> "μs"
            MILLISECONDS -> "ms"
            SECONDS      -> "s"
            MINUTES      -> "m"
            HOURS        -> "h"
            DAYS         -> "d"
            else         -> NEVER
        }
val Int.ms get() = milliseconds
val Int.sec get() = seconds
val Int.min get() = minutes
val Long.ms get() = milliseconds
val Long.sec get() = seconds
val Long.min get() = minutes
val Double.ms get() = milliseconds
val Double.sec get() = seconds
val Double.min get() = minutes
val Number.ms
    get() =
        when (this) {
            is Int    -> ms
            is Long   -> ms
            is Double -> ms
            else      -> NOT_IMPLEMENTED
        }
val Number.sec
    get() =
        when (this) {
            is Int    -> sec
            is Long   -> sec
            is Double -> sec
            else      -> NOT_IMPLEMENTED
        }
val Number.min
    get() =
        when (this) {
            is Int    -> min
            is Long   -> min
            is Double -> min
            else      -> NOT_IMPLEMENTED
        }
val Number.hours
    get() =
        when (this) {
            is Int    -> hours
            is Long   -> hours
            is Double -> hours
            else      -> NOT_IMPLEMENTED
        }
val Number.days
    get() =
        when (this) {
            is Int    -> days
            is Long   -> days
            is Double -> days
            else      -> NOT_IMPLEMENTED
        }
