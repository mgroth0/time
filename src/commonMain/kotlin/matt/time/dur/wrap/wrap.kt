package matt.time.dur.wrap

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializer
import matt.lang.convert.BiConverter
import matt.model.data.mathable.MathAndComparable
import matt.time.toUnixTime
import kotlin.jvm.JvmInline
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit
import kotlin.time.DurationUnit.SECONDS


object DurationByWrapperSerializer : KSerializer<DurationWrapper> {

    private val durSer by lazy {
        serializer<Duration>()
    }
    override val descriptor by lazy {
        durSer.descriptor
    }

    override fun deserialize(decoder: Decoder): DurationWrapper {
        val inner = decoder.decodeSerializableValue(durSer)
        return inner.wrapped()
    }

    override fun serialize(
        encoder: Encoder,
        value: DurationWrapper
    ) {
        encoder.encodeSerializableValue(durSer, value.dur)
    }
}


fun Duration.wrapped() = DurationWrapper(this)

@Serializable(with = DurationByWrapperSerializer::class)
@JvmInline
value class DurationWrapper(val dur: Duration) :
    MathAndComparable<DurationWrapper> {

    companion object {
        val ZERO = DurationWrapper(Duration.ZERO)
    }

    override fun toString() = dur.toString()

    fun toString(
        unit: DurationUnit,
        decimals: Int = 0
    ) = dur.toString(unit, decimals)


    fun toDouble(unit: DurationUnit) = dur.toDouble(unit)

    val inWholeMilliseconds get() = dur.inWholeMilliseconds
    val inWholeSeconds get() = dur.inWholeSeconds
    val inDecimalSeconds get() = dur.toDouble(SECONDS)
    override fun plus(m: DurationWrapper): DurationWrapper = (dur + m.dur).wrapped()

    override fun minus(m: DurationWrapper): DurationWrapper = (dur - m.dur).wrapped()

    override fun div(m: DurationWrapper): Double = (dur / m.dur)
    override fun floatingPointDiv(m: DurationWrapper): Double = this / m


    override fun div(n: Number): DurationWrapper = (dur / n.toDouble()).wrapped()

    override fun times(n: Number): DurationWrapper = (dur * n.toDouble()).wrapped()

    override val abs: DurationWrapper
        get() = dur.absoluteValue.wrapped()



    override fun compareTo(other: DurationWrapper): Int = dur.compareTo(other.dur)

    fun remMillis(d: DurationWrapper): DurationWrapper = inWholeMilliseconds.rem(d.inWholeMilliseconds).milliseconds.wrapped()


    fun toUnixTime() = dur.toUnixTime()

    @Suppress("NOTHING_TO_INLINE")
    inline fun sleep() = matt.time.dur.sleep(dur)
}



object MilliSecondDurationWrapperConverter : BiConverter<DurationWrapper, Double> {
    override fun convertToB(a: DurationWrapper): Double = a.inWholeMilliseconds.toDouble()

    override fun convertToA(b: Double): DurationWrapper = b.milliseconds.wrapped()
}


@Suppress("NOTHING_TO_INLINE")
inline operator fun Int.times(duration: DurationWrapper): DurationWrapper = duration * this


