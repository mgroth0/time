package matt.time

import kotlinx.datetime.toKotlinInstant
import kotlin.js.Date

actual fun nowKotlinDateTime() = Date().toKotlinInstant().toLocalDateTime()