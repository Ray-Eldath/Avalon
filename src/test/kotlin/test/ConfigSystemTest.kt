@file:JvmName("ConfigSystemTest")

package test

import avalon.tool.ObjectCaster.toLongArray
import avalon.tool.system.Configs

/**
 * Created by Eldath Ray on 2017/3/18 0018.
 *
 * @author Eldath Ray
 */
fun main(args: Array<String>) {
	val test = toLongArray(Configs.getConfigArray("Admin_Uid"))
	println(Configs["Block_Words"])
	test.forEach(::println)
}
