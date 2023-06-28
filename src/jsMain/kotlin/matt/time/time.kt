package matt.time

import kotlinx.datetime.toKotlinInstant
import matt.time.toLocalDateTime
import kotlin.js.Date

actual fun nowKotlinDateTime() = Date().toKotlinInstant().toLocalDateTime()