package matt.time.cron

import kotlinx.serialization.Serializable
import matt.lang.anno.SeeURL
import matt.model.ser.EncodedAsStringKSerializer
import matt.prim.str.removePrefixAndOrSuffix

object PosixCronSerializer : EncodedAsStringKSerializer<PosixCron>() {

    override fun String.decode(): PosixCron {
        val s = this
        val parts = s.split(" ")
        return PosixCron(
            minutes = parts[0], hours = parts[1], dayOfMonth = parts[2], month = parts[3], dayOfWeek = parts[4]
        )
    }

    override fun PosixCron.encodeToString(): String = format()


}


@Serializable(with = PosixCronSerializer::class)
@SeeURL("https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#schedule")
@SeeURL("https://pubs.opengroup.org/onlinepubs/9699919799/utilities/crontab.html#tag_20_25_07")
data class PosixCron(
    val minutes: String,
    val hours: String,
    val dayOfMonth: String,
    val month: String,
    val dayOfWeek: String,
) {
    companion object {
        val EVERY_THREE_AM by lazy {
            PosixCron(
                minutes = "0", hours = "3", dayOfMonth = "*", month = "*", dayOfWeek = "*"
            )
        }
    }

    fun format() = listOf(
        minutes,
        hours,
        dayOfMonth,
        month,
        dayOfWeek,
    ).joinToString(separator = " ")
}

object QuotedPosixCronSerializer : EncodedAsStringKSerializer<QuotedPosixCron>() {

    internal const val QUOTE_CHAR = "'"

    override fun String.decode(): QuotedPosixCron {
        val s = this
        val parts = s.removePrefixAndOrSuffix(QUOTE_CHAR).split(" ")
        return QuotedPosixCron(
            PosixCron(
                minutes = parts[0], hours = parts[1], dayOfMonth = parts[2], month = parts[3], dayOfWeek = parts[4]
            )
        )
    }

    override fun QuotedPosixCron.encodeToString(): String = format()

}


@Serializable(with = QuotedPosixCronSerializer::class)
class QuotedPosixCron(val posixCron: PosixCron) {
    fun format() = QuotedPosixCronSerializer.QUOTE_CHAR + posixCron.format() + QuotedPosixCronSerializer.QUOTE_CHAR
}
