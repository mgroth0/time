@file:JvmName("TimeJvmKt")

package matt.time

import kotlinx.datetime.toKotlinLocalDateTime
import matt.time.dur.nowDateTime

actual fun nowKotlinDateTime() = nowDateTime().toKotlinLocalDateTime()