package avalon.plugin

fun main(args: Array<String>) {
	val status = CIs.get("TravisCI")!!.getStatus("Ray-Eldath/Avalon")
	println(status)
}