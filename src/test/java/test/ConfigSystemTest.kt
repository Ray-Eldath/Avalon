@file:JvmName("ConfigSystemTest")

package test

import avalon.tool.system.Config

import avalon.tool.ObjectCaster.toLongArray

/**
 * Created by Eldath Ray on 2017/3/18 0018.
 *
 * @author Eldath Ray
 */
fun main(args: Array<String>) {
	val test = toLongArray(Config.getConfigArray("Admin_Uid"))
	println(Config["Block_Words"])
	test.forEach(::println)
}
