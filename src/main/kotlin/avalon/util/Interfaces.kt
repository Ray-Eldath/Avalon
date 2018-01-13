package avalon.util

interface Displayable {
	val string: String
}

interface Service {
	fun available(): Boolean
}