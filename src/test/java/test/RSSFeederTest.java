package test;

import avalon.extend.RSSFeeder;

public class RSSFeederTest {
	public static void main(String[] args) throws InterruptedException {
		for (; ; ) {
			RSSFeeder.INSTANCE.run();
			Thread.sleep(1000);
		}
	}
}
