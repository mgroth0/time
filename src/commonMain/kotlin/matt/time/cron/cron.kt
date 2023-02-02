package matt.time.cron

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializer
import matt.lang.anno.SeeURL

@OptIn(InternalSerializationApi::class)
object PosixCronSerializer: KSerializer<PosixCron> {
  override val descriptor by lazy {
	String::class.serializer().descriptor
  }

  override fun deserialize(decoder: Decoder): PosixCron {
	TODO("Not yet implemented")
  }

  override fun serialize(encoder: Encoder, value: PosixCron) {
	encoder.encodeString(value.format())
  }

}

@Serializable
@SeeURL("https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#schedule")
@SeeURL("https://pubs.opengroup.org/onlinepubs/9699919799/utilities/crontab.html#tag_20_25_07")
data class PosixCron(
  val minutes: String,
  val hours: String,
  val dayOfMonth: String,
  val month: String,
  val dayOfWeek: String,
) {
  fun format() = listOf(
	minutes,
	hours,
	dayOfMonth,
	month,
	dayOfWeek,
  ).joinToString(separator = " ")
}