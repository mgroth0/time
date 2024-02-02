package matt.time.dur

import matt.lang.NEVER
import kotlin.math.floor
import kotlin.time.Duration
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

expect fun sleep(duration: Duration)
fun sleepForever(print: Boolean = false) {
    if (print) println("Sleeping Forever")
    sleep(Duration.INFINITE)
}

expect suspend fun multiPlatformSleep(duration: Duration)

fun Duration.formatForSpeechSecs() = "${this.inWholeSeconds} seconds"
fun Duration.formatForSpeechMins() = "${this.inWholeMinutes} minutes"

fun Duration.formatForSpeech() = when {
    this == 1.nanoseconds  -> "1 nanosecond"
    this == 1.microseconds -> "1 microsecond"
    this < 2.microseconds  -> "${this.inWholeNanoseconds} nanoseconds"
    this == 1.milliseconds -> "1 millisecond"
    this < 2.milliseconds  -> "${this.inWholeMicroseconds} microseconds"
    this == 1.seconds      -> "1 second"
    this < 2.seconds       -> "${this.inWholeMilliseconds} milliseconds"
    this == 1.minutes      -> "1 minute"
    this < 1.minutes       -> "${this.inWholeMinutes} minutes"
    this < 2.minutes       -> "${this.minutesPart} minute, ${this.secondsPart} seconds"
    this < 1.hours         -> "${this.minutesPart} minutes, ${this.secondsPart} seconds"
    this == 1.hours        -> "1 hour"
    this < 2.hours         -> "${this.hoursPart} hour, ${this.minutesPart} minutes, ${this.secondsPart} seconds"
    this < 24.hours        -> "${this.hoursPart} hours, ${this.minutesPart} minutes, ${this.secondsPart} seconds"
    else                   -> "$this"
}

val Duration.hoursPart: Long
    get() = when {
        this < 1.hours  -> 0
        this == 1.hours -> 1
        else            -> floor((this.inWholeHours % 24.0)).toLong()
    }
val Duration.minutesPart: Long
    get() = when {
        this < 1.minutes  -> 0
        this == 1.minutes -> 1
        else              -> floor((this.inWholeMinutes % 60.0)).toLong()
    }
val Duration.secondsPart: Long
    get() = when {
        this < 1.seconds  -> 0
        this == 1.seconds -> 1
        else              -> floor((this.inWholeSeconds % 60.0)).toLong()
    }
val Duration.millisecondsPart: Long
    get() = when {
        this < 1.milliseconds  -> 0
        this == 1.milliseconds -> 1
        else                   -> floor((this.inWholeMilliseconds % 1000.0)).toLong()
    }

val Duration.isZero get() = this == Duration.ZERO
val Duration.isNotZero get() = !isZero


fun Duration.largestUnitAtLeastOne(): DurationUnit {


    require(!isNegative())
    return when {
        inWholeDays >= 1         -> TODO("get largest unit at least one for $this")
        inWholeHours >= 1        -> DurationUnit.HOURS
        inWholeMinutes >= 1      -> DurationUnit.MINUTES
        inWholeSeconds >= 1      -> DurationUnit.SECONDS
        inWholeMilliseconds >= 1 -> DurationUnit.MILLISECONDS
        inWholeMicroseconds >= 1 -> DurationUnit.MICROSECONDS
        else                     -> {
            check(inWholeNanoseconds >= 1)
            DurationUnit.NANOSECONDS
        }
    }
}

val DurationUnit.unitLabelTwoCharMax
    get() = when (this) {
        NANOSECONDS  -> "ns"
        MICROSECONDS -> "μs"
        MILLISECONDS -> "ms"
        SECONDS      -> "s"
        MINUTES      -> "m"
        HOURS        -> "h"
        DAYS         -> "d"
        else         -> NEVER
    }

