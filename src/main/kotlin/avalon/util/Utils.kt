package avalon.util

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.pattern.color.ANSIConstants.*
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase


class HighLightConverter : ForegroundCompositeConverterBase<ILoggingEvent>() {
	override fun getForegroundColorCode(event: ILoggingEvent): String {
		return when (event.level.toInt()) {
			Level.ERROR_INT -> BOLD + RED_FG
			Level.WARN_INT -> RED_FG
			else -> DEFAULT_FG
		}
	}
}

class LoggerHighLightConverter : ForegroundCompositeConverterBase<ILoggingEvent>() {
	override fun getForegroundColorCode(event: ILoggingEvent): String {
		return when (event.level.toInt()) {
			Level.WARN_INT -> CYAN_FG
			Level.ERROR_INT -> BOLD + CYAN_FG
			else -> WHITE_FG
		}
	}
}