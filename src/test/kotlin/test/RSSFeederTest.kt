package test

import avalon.plugin.RSSFeeder

fun main(args: Array<String>) {
	while (true) {
		RSSFeeder.run()
		Thread.sleep(1000)
	}
}
