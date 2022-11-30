package matt.time.dur

import kotlin.math.floor
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.microseconds
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds

expect fun sleep(duration: Duration)

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
	else            -> floor((this.inWholeHours/24.0)).toLong()
  }
val Duration.minutesPart: Long
  get() = when {
	this < 1.minutes  -> 0
	this == 1.minutes -> 1
	else              -> floor((this.inWholeMinutes%60.0)).toLong()
  }
val Duration.secondsPart: Long
  get() = when {
	this < 1.seconds  -> 0
	this == 1.seconds -> 1
	else              -> floor((this.inWholeSeconds%60.0)).toLong()
  }