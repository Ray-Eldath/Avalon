package test

import avalon.extend.RSSFeeder

fun main(args: Array<String>) {
	while (true) {
		RSSFeeder.run()
		Thread.sleep(1000)
	}
}
