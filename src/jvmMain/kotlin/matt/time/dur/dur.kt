@file:JvmName("DurJvmKt")

package matt.time.dur

import kotlinx.coroutines.delay
import matt.lang.NOT_IMPLEMENTED
import matt.lang.RUNTIME_MX
import matt.model.op.convert.StringConverter
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

//fun Duration.toMDuration() = this.inWholeMilliseconds.milli

fun uptime() = RUNTIME_MX.uptime.milliseconds


val Number.unixSeconds: Date
  get() = Date((this.toDouble()*1000).toLong())
val Number.unixMS: Date
  get() = Date(this.toLong())


private val stupid = "Have to keep it as a different name than Duration.format since they are in the same package???"

const val myDateFormatStr = "EEE, MMM d, h:mm a"
val myDateTimeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern(myDateFormatStr)
fun Date.formatDate(): String = SimpleDateFormat(myDateFormatStr).format(this)
fun today(): LocalDate = LocalDate.now()
fun tomorrow(): LocalDate = today().plus(1, ChronoUnit.DAYS)
fun nowDateTime(): LocalDateTime = today().atTime(LocalTime.now())

fun localDateTimeOfEpochMilli(ms: Long): LocalDateTime =
  LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), ZoneId.systemDefault())

fun milli() = System.currentTimeMillis()

fun LocalDateTime.atTime(hour: Int, min: Int): LocalDateTime = toLocalDate().atTime(hour, min)
private val OFFSET: ZoneOffset = OffsetDateTime.now().offset
fun LocalDateTime.toEpochMilli() = toEpochSecond(OFFSET)*1000

operator fun Date.minus(started: Date): Duration {
  return this.toInstant() - started.toInstant()
}

operator fun Instant.minus(started: Instant): Duration {
  return (this.epochSecond - started.epochSecond).seconds
}

operator fun Date.minus(started: Instant): Duration {
  return toInstant() - started
}

operator fun Instant.minus(started: Date): Duration {
  return this - started.toInstant()
}


/*val Number.nanos
  get() = Duration.ofNanoseconds(this)
*/

val Number.ms
  get() = when (this) {
	is Int    -> ms
	is Long   -> ms
	is Double -> ms
	else      -> NOT_IMPLEMENTED
  }
val Number.sec
  get() = when (this) {
	is Int    -> sec
	is Long   -> sec
	is Double -> sec
	else      -> NOT_IMPLEMENTED
  }
val Number.min
  get() = when (this) {
	is Int    -> min
	is Long   -> min
	is Double -> min
	else      -> NOT_IMPLEMENTED
  }
val Number.hours
  get() = when (this) {
	is Int    -> hours
	is Long   -> hours
	is Double -> hours
	else      -> NOT_IMPLEMENTED
  }
val Number.days
  get() = when (this) {
	is Int    -> days
	is Long   -> days
	is Double -> days
	else      -> NOT_IMPLEMENTED
  }

val Int.ms get() = this.milliseconds
val Int.sec get() = this.seconds
val Int.min get() = this.minutes

val Long.ms get() = this.milliseconds
val Long.sec get() = this.seconds
val Long.min get() = this.minutes

val Double.ms get() = this.milliseconds
val Double.sec get() = this.seconds
val Double.min get() = this.minutes

/*val Number.hours
  get() = Duration.ofHours(this)
val Number.hour
  get() = hours
val Number.days
  get() = Duration.ofDays(this)
val Number.day
  get() = days*/

fun now() = System.currentTimeMillis().unixMS


@Suppress("NOTHING_TO_INLINE")
actual inline fun sleep(duration: Duration) = Thread.sleep(duration.inWholeMilliseconds)

actual suspend fun multiPlatformSleep(duration: Duration) {
  delay(duration)
}

@Suppress("NOTHING_TO_INLINE")
@JvmName("sleep2")
inline fun Duration.sleep() = sleep(this)


val UNIX_MS_FORMATTER = object: StringConverter<Number> {
  override fun toString(t: Number): String {
	return Date(t.toLong()).formatDate()
  }

  override fun fromString(s: String) = TODO()
}