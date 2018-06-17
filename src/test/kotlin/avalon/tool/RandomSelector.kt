package avalon.tool

import avalon.util.backend.CoolQBackend
import java.time.LocalDateTime
import java.time.ZoneOffset

object RandomSelector {
	@JvmStatic
	fun main(args: Array<String>) {
		val time = LocalDateTime.of(2018, 6, 10, 23, 59)
		val list = CoolQBackend.INSTANCE().getGroupMembersCards(617118724, time.toEpochSecond(ZoneOffset.of("+8")))

		println("List: (filtered and thus list only contains those spoke after ${time.toString().replace("T", " ")})")
		list.forEach { println("\t$it") }
		list.shuffle()
		println("Result:\n\t${list[0]}")
	}
}