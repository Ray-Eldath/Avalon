package test

import avalon.tool.system.GroupConfig
import org.apache.commons.lang3.ArrayUtils.toString

fun main(args: Array<String>) {
	val config = GroupConfig.instance().getConfig(399863405)
	println(toString(config.admin))
	println(toString(config.blacklist))
	config.permissions.forEach { println(it) }
	println(config.isListen)
	println(config.isRecord)
}
