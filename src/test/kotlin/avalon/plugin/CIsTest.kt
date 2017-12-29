package avalon.plugin

fun main(args: Array<String>) {
	val travisCI = CIs.TravisCI()
	val status = travisCI.getStatus("Ray-Eldath/Avalon")
	println(status)
}