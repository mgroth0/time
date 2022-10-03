package matt.time.dur

import matt.lang.RUNTIME_MX
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
import kotlin.time.Duration.Companion.milliseconds
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


//val Number.nanos
//  get() = Duration.ofNanoseconds(this)
//val Number.ms
//  get() = Duration.ofMilliseconds(this)
//val Number.sec
//  get() = Duration.ofSeconds(this)
//val Number.min
//  get() = Duration.ofMinutes(this)
//val Number.hours
//  get() = Duration.ofHours(this)
//val Number.hour
//  get() = hours
//val Number.days
//  get() = Duration.ofDays(this)
//val Number.day
//  get() = days

fun now() = System.currentTimeMillis().unixMS


fun sleep(d: Duration) = Thread.sleep(d.inWholeMilliseconds)