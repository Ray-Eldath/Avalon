package avalon.util

class ConfigurationError(message: String?) : RuntimeException(message)

class HTTPRequestCodeException(code: Int?) : Exception()