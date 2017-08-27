package avalon.extend;

import avalon.tool.pool.ConstantPool;
import avalon.tool.system.ConfigSystem;
import avalon.tool.system.GroupConfigSystem;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

public class RSSFeeder implements Runnable {
	private List<URL> urls = toURLList(ConfigSystem.getInstance().getCommandConfigArray("RSS", "feed"));
	private List<Integer> sent = new ArrayList<>();
	private Stack<RSSParser.RSSItem> items = new Stack<>();
	private static RSSFeeder instance = null;

	public static RSSFeeder getInstance() {
		if (instance == null) instance = new RSSFeeder();
		return instance;
	}

	private RSSFeeder() {
	}

	private boolean update() {
		List<RSSParser.RSSItem> items = getAllItems();
		items.sort(Comparator.comparing(RSSParser.RSSItem::pubDate));
		boolean returnValue = false;
		for (RSSParser.RSSItem thisItem : items) {
			int hashcode = thisItem.hashCode();
			if (!sent.contains(hashcode)) {
				this.items.push(thisItem);
				sent.add(hashcode);
				returnValue = true;
			}
		}
		return returnValue;
	}

	private List<RSSParser.RSSItem> getAllItems() {
		List<RSSParser.RSSItem> allItems = new ArrayList<>();
		for (URL thisURL : urls)
			allItems.addAll(RSSParser.get(thisURL));
		return allItems;
	}

	private static List<URL> toURLList(Object[] in) {
		List<URL> result = new ArrayList<>();
		for (Object anIn : in)
			if (anIn instanceof String)
				try {
					result.add(new URL((String) anIn));
				} catch (MalformedURLException e) {
					throw new RuntimeException(e);
				}
		return result;
	}

	private RSSParser.RSSItem newest() {
		return items.pop();
	}

	@Override
	public void run() {
		if (update()) {
			RSSParser.RSSItem newest = newest();
			for (long groupUid : GroupConfigSystem.instance().getFollowGroups())
				ConstantPool.Basic.currentServlet.responseGroup(groupUid,
						String.format("订阅的RSS %s - %s 有更新：\n%s\n发布时间：%s 详见：%s",
								newest.info().title(),
								newest.info().description(),
								newest.title(),
								newest.pubDate().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)),
								newest.link()));
		}
	}
}
