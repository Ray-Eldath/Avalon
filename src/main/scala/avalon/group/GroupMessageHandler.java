package avalon.group;

import avalon.api.CustomGroupResponder;
import avalon.extend.Recorder;
import avalon.main.MessageChecker;
import avalon.tool.APIRateLimit;
import avalon.tool.RunningData;
import avalon.tool.pool.APISurvivePool;
import avalon.tool.pool.AvalonPluginPool;
import avalon.tool.pool.ConstantPool;
import avalon.tool.pool.VariablePool;
import avalon.tool.system.ConfigSystem;
import avalon.util.GroupMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.LongStream;

import static avalon.main.RegisterResponder.register;
import static avalon.tool.ObjectCaster.toLongArray;
import static avalon.tool.ObjectCaster.toStringArray;
import static avalon.tool.Responder.AT;

/**
 * Created by Eldath Ray on 2017/3/30.
 *
 * @author Eldath Ray
 */
public class GroupMessageHandler {
	private static final Map<Pattern, GroupMessageResponder> apiList = new LinkedHashMap<>();
	private static final Map<Pattern, CustomGroupResponder> customApiList = new LinkedHashMap<>();
	private static final Map<? super GroupMessageResponder, Boolean> enableMap = new HashMap<>();

	private static long[] adminUid = toLongArray(ConfigSystem
			.getInstance().getConfigArray("Admin_Uid"));
	private static long[] followGroup = toLongArray(ConfigSystem
			.getInstance().getConfigArray("Follow_Group_Uid"));
	private static long[] recordGroup = toLongArray(ConfigSystem
			.getInstance().getConfigArray("Record_Group_Uid"));
	private static long[] blackListPeople = toLongArray(ConfigSystem
			.getInstance().getConfigArray("BlackList_Uid"));
	private static Map<Long, Integer> blackListPeopleMap = new HashMap<>();
	private static String[] blockList = toStringArray(ConfigSystem
			.getInstance().getConfigArray("Block_Words"));
	private static final int punishFrequency = (int) ConfigSystem.getInstance()
			.get("Block_Words_Punish_Frequency");
	private static final APIRateLimit cooling = new APIRateLimit(3000L);

	private static GroupMessageHandler instance = new GroupMessageHandler();
	private static final Logger logger = LoggerFactory.getLogger(GroupMessageHandler.class);

	Map<Pattern, GroupMessageResponder> getApiList() {
		return apiList;
	}

	public static GroupMessageHandler getInstance() {
		return instance;
	}

	private GroupMessageHandler() {
		for (long thisBlackPeople : blackListPeople)
			blackListPeopleMap.put(thisBlackPeople, punishFrequency + 1);
		int length = followGroup.length;
		long[] data = new long[length + 1];
		System.arraycopy(followGroup, 0, data, 1, length);
		data[0] = 100000;
		followGroup = data;
		//
		enableMap.put(AnswerMe.instance(), ConstantPool.Setting.AnswerMe_Enabled);
		enableMap.put(Wolfram.instance(), ConstantPool.Setting.Wolfram_Enabled);
		enableMap.put(Execute.getInstance(), ConstantPool.Setting.Execute_Enable);
	}

	static {
	    /*
	     * 指令优先级排序依据：单词 >> 多词，管理类 >> 服务类 >> 娱乐类，触发类 >> 自由类
         */
		// 特殊优先
		register(Test.instance());
		// 管理类
		register(Shutdown.instance());
		register(Flush.instance());
		register(Manager.instance());
		register(Blacklist.instance());
		// 服务类
		register(Help.getInstance());
		register(Version.instance());
		register(ShowAdmin.instance());
		register(Echo.instance());
		register(Execute.getInstance());
		// 娱乐类
		register(Wolfram.instance());
		register(Mo.instance());
		register(AnswerMe.instance());
	}

	GroupMessageResponder getGroupResponderByKeyword(String keyword) {
		for (Map.Entry<Pattern, GroupMessageResponder> patternAPIEntry : apiList.entrySet()) {
			Pattern key = patternAPIEntry.getKey();
			GroupMessageResponder value = patternAPIEntry.getValue();
			if (key.matcher(keyword).find())
				return value;
		}
		return null;
	}

	boolean isResponderEnable(GroupMessageResponder api) {
		if (!enableMap.containsKey(api))
			return true;
		return enableMap.get(api);
	}

	public void handle(GroupMessage message) {
		if (!ConstantPool.Basic.debug)
			Recorder.getInstance().recodeGroupMessage(message);
		//FIXME MessageHooker.handle(message);
		long groupUid = message.getGroupUid();
		String sender = message.getSenderNickName();
		long senderUid = message.getSenderUid();
		LongStream adminUidStream = Arrays.stream(adminUid);
		LongStream followGroupStream = Arrays.stream(followGroup);
		boolean admin = adminUidStream.anyMatch(e -> e == senderUid);
		if (followGroupStream.anyMatch(e -> e == groupUid)) {
			if (ConstantPool.Setting.Block_Words_Punishment_Mode_Enabled)
				if (blackListPeopleMap.containsKey(senderUid)) {
					if (blackListPeopleMap.get(senderUid) >= punishFrequency) {
						logger.info("Account " + senderUid + ":" + sender + " was blocked. Please entered " +
								"\"avalon blacklist remove " + senderUid + "\" to the group " + groupUid + ":" +
								message.getGroupName() + " if you really want to unblock this account.");
						if (!admin)
							message.response("@" + sender +
									" 您的帐号由于发送过多不允许关键字，现已被屏蔽~o(╯□╰)o！");
						return;
					}
				} else blackListPeopleMap.put(senderUid, 0);
			for (Map.Entry<Pattern, GroupMessageResponder> patternAPIEntry : apiList.entrySet()) {
				GroupMessageResponder value = patternAPIEntry.getValue();
				if (doCheck(patternAPIEntry.getKey(), message)) {
					if (!APISurvivePool.getInstance().isSurvive(value)) {
						if (!APISurvivePool.getInstance().isNoticed(value)) {
							message.response(AT(message) + " 对不起，您调用的指令响应器目前已被停止；" +
									"注意：此消息仅会显示一次。");
							APISurvivePool.getInstance().setNoticed(value);
						}
					} else if (MessageChecker.check(message) && isResponderEnable(value))
						value.doPost(message);
					return;
				}
			}

			for (Map.Entry<Pattern, CustomGroupResponder> patternAPIEntry : customApiList.entrySet()) {
				CustomGroupResponder value = patternAPIEntry.getValue();
				if (doCheck(patternAPIEntry.getKey(), message)) {
					if (MessageChecker.check(message))
						value.doPost(message);
					return;
				}
			}
		}
		if (Arrays.stream(recordGroup).anyMatch(e -> e == groupUid))
			Recorder.getInstance().recodeGroupMessage(message);
	}

	private boolean doCheck(Pattern key, GroupMessage groupMessage) {
		String lowerContent = groupMessage.getContent().toLowerCase();
		long time = groupMessage.getTimeLong();
		String sender = groupMessage.getSenderNickName();
		if (!key.matcher(lowerContent).find())
			return false;
		for (String thisBlockString : blockList)
			if (groupMessage.getContent().
					trim().
					toLowerCase().
					replaceAll("[\\pP\\p{Punct}]", "").contains(thisBlockString)) {
				String notice = "您发送的消息含有不允许的关键词！";
				if (ConstantPool.Setting.Block_Words_Punishment_Mode_Enabled) {
					notice = "您发送的消息含有不允许的关键词，注意：" + punishFrequency +
							"次发送不允许关键词后帐号将被屏蔽！⊙﹏⊙!";
					blackListPlus(groupMessage.getSenderUid());
				}
				groupMessage.response(AT(groupMessage) + " " + notice);
				return false;
			}
		if (!cooling.trySet(time)) {
			if (!VariablePool.Limit_Noticed) {
				groupMessage.response("@" + sender +
						" 对不起，您的指令超频。3s内仅能有一次指令输入，未到3s内的输入将被忽略。注意：此消息仅会显示一次。");
				VariablePool.Limit_Noticed = true;
			}
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		ConfigSystem.getInstance();
		RunningData.getInstance();
		new ConstantPool.Basic();
		new ConstantPool.Address();
		AvalonPluginPool.load();
		if (!ConstantPool.Basic.debug)
			return;
		Scanner scanner = new Scanner(System.in);
		int id = 0;
		//noinspection InfiniteLoopStatement
		while (true) {
			System.out.print("Input here:");
			String content = scanner.nextLine();
			GroupMessage message = new GroupMessage(++id, LocalDateTime.now(),
					10000, "Test", 100000, "Test Group", content);
			GroupMessageHandler.getInstance().handle(message);
			System.out.println("===");
		}
	}

	private void blackListPlus(long senderUid) {
		int pastValue = blackListPeopleMap.get(senderUid);
		blackListPeopleMap.put(senderUid, ++pastValue);
	}

	public static void addGroupMessageResponder(GroupMessageResponder responder) {
		apiList.put(responder.getKeyWordRegex(), responder);
	}

	public static void addCustomGroupResponder(CustomGroupResponder responder) {
		customApiList.put(responder.getKeyWordRegex(), responder);
	}

	static Map<Pattern, CustomGroupResponder> getCustomApiList() {
		return customApiList;
	}

	public static long[] getAdminUid() {
		return adminUid.clone();
	}

	public long[] getFollowGroup() {
		return followGroup;
	}

	static int getPunishFrequency() {
		return punishFrequency;
	}

	static Map<Long, Integer> getSetBlackListPeopleMap() {
		return blackListPeopleMap;
	}
}
