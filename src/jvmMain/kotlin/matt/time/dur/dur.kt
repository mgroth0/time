package matt.time.dur

import matt.lang.RUNTIME_MX
import matt.math.BILLION
import matt.math.MILLION
import matt.math.Mathable
import matt.math.THOUSAND
import matt.math.roundToDecimal
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
import kotlin.time.toKotlinDuration

fun kotlin.time.Duration.toMDuration() = this.inWholeMilliseconds.ms

class Duration private constructor(nanos: Long): Comparable<Duration>, Mathable<Duration> {

  constructor(startNanos: Number, stopNanos: Number): this(
	(stopNanos.toDouble() - startNanos.toDouble()).toLong()
  )

  private val stupidDur: java.time.Duration = java.time.Duration.ofNanos(nanos)
  fun toKotlinDuration() = stupidDur.toKotlinDuration()

  companion object {
	val purpose =
	  "because stupid kotlin duration is \"experimental\", requiring weird annotations and not working half the time, while stupid java matt.time.dur.Duration is even more stupid because all the methods take and give whole numbers rather than Doubles. The nice thing is if I eventually want to switch to kotlin.time.matt.time.dur.Duration backend, it wont be a huge task"

	fun ofDays(days: Number) = Duration((days.toDouble()*60*60*24*BILLION).toLong())
	fun ofHours(hours: Number) = Duration((hours.toDouble()*60*60*BILLION).toLong())
	fun ofMinutes(min: Number) = Duration((min.toDouble()*60*BILLION).toLong())
	fun ofSeconds(sec: Number) = Duration((sec.toDouble()*BILLION).toLong())
	fun ofMilliseconds(ms: Number) = Duration((ms.toDouble()*MILLION).toLong())
	fun ofNanoseconds(nanos: Number) = Duration(nanos.toLong())

	fun uptime() = ofMilliseconds(RUNTIME_MX.uptime)
  }


  val inMinutes by lazy {
	THOUSAND
	stupidDur.toNanos().toDouble()/MILLION/THOUSAND/60.0
  }
  val inSeconds by lazy {
	stupidDur.toNanos().toDouble()/MILLION/THOUSAND
  }
  val inMilliseconds by lazy {
	stupidDur.toNanos().toDouble()/MILLION
  }
  val inMicroseconds by lazy {
	stupidDur.toNanos().toDouble()/THOUSAND
  }
  val inNanoseconds by lazy {
	stupidDur.toNanos().toDouble()
  }

  fun format(): String {
	return when {
	  inMinutes >= 2.0      -> "${inMinutes.roundToDecimal(2)} min"
	  inSeconds >= 2.0      -> "${inSeconds.roundToDecimal(2)} sec"
	  inMilliseconds >= 2.0 -> "${inMilliseconds.roundToDecimal(2)} ms"
	  inMicroseconds >= 2.0 -> "${inMicroseconds.roundToDecimal(2)} μs"
	  else                  -> "${inNanoseconds.roundToDecimal(2)} ns"

	}
  }

  override fun compareTo(other: Duration): Int {
	return this.stupidDur.compareTo(other.stupidDur)
  }

  override fun plus(m: Duration): Duration {
	return Duration((stupidDur + m.stupidDur).toNanos())
  }

  override fun div(n: Number): Duration {
	return Duration((stupidDur.toNanos()/n.toDouble()).toLong())
  }

  override fun toString() = format()

}


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
  return (this.epochSecond - started.epochSecond).sec
}

operator fun Date.minus(started: Instant): Duration {
  return toInstant() - started
}

operator fun Instant.minus(started: Date): Duration {
  return this - started.toInstant()
}


val Number.nanos
  get() = Duration.ofNanoseconds(this)
val Number.ms
  get() = Duration.ofMilliseconds(this)
val Number.sec
  get() = Duration.ofSeconds(this)
val Number.min
  get() = Duration.ofMinutes(this)
val Number.hours
  get() = Duration.ofHours(this)
val Number.hour
  get() = hours
val Number.days
  get() = Duration.ofDays(this)
val Number.day
  get() = days

fun now() = System.currentTimeMillis().unixMS


fun sleep(d: kotlin.time.Duration) = Thread.sleep(d.inWholeMilliseconds)