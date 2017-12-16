package avalon.group;

import avalon.api.CustomGroupResponder;
import avalon.extend.Recorder;
import avalon.main.MessageChecker;
import avalon.tool.APIRateLimit;
import avalon.tool.ObjectCaster;
import avalon.tool.pool.APISurvivePool;
import avalon.tool.pool.AvalonPluginPool;
import avalon.tool.pool.Constants;
import avalon.tool.pool.Variables;
import avalon.tool.system.Config;
import avalon.tool.system.GroupConfig;
import avalon.tool.system.RunningData;
import avalon.util.GroupMessage;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.LongStream;

import static avalon.api.Flag.AT;
import static avalon.api.RegisterResponder.register;
import static avalon.tool.ObjectCaster.toStringArray;
import static avalon.tool.pool.Constants.Basic.DEBUG_MESSAGE_UID;

/**
 * Created by Eldath Ray on 2017/3/30.
 *
 * @author Eldath Ray
 */
public class GroupMessageHandler {


	private static final Map<Pattern, GroupMessageResponder> apiList = new LinkedHashMap<>();
	private static final Map<Pattern, CustomGroupResponder> customApiList = new LinkedHashMap<>();
	private static final Map<? super GroupMessageResponder, Boolean> enableMap = new HashMap<>();

	private static Map<Long, Integer> publishPeopleMap = new HashMap<>();

	private static final String[] blockWordList = toStringArray(Config.Companion.instance().getConfigArray("block_words"));
	private static final int punishFrequency = (int) Config.Companion.instance().get("block_words_punish_frequency");
	private static final long coolingDuration = ObjectCaster.toLong(Config.INSTANCE.get("cooling_duration"));

	private static final APIRateLimit cooling = new APIRateLimit(coolingDuration);
	private static GroupMessageHandler instance = new GroupMessageHandler();
	private static final Logger LOGGER = LoggerFactory.getLogger(GroupMessageHandler.class);

	public static Map<Pattern, GroupMessageResponder> getApiList() {
		return apiList;
	}

	public static GroupMessageHandler getInstance() {
		return instance;
	}

	private GroupMessageHandler() {
		enableMap.put(AnswerMe.INSTANCE, Constants.Setting.AnswerMe_Enabled);
		enableMap.put(Wolfram.INSTANCE, Constants.Setting.Wolfram_Enabled);
		enableMap.put(Execute.INSTANCE, Constants.Setting.Execute_Enabled);
		enableMap.put(Hitokoto.INSTANCE, Constants.Setting.Hitokoto_Enabled);
		enableMap.put(Quote.INSTANCE, Constants.Setting.Quote_Enabled);
	}

	static {
		/*
		 * 指令优先级排序依据：单词 >> 多词，管理类 >> 服务类 >> 娱乐类，触发类 >> 自由类
		 */
		// 管理类
		register(Shutdown.INSTANCE);
		register(Flush.INSTANCE);
		register(Manager.INSTANCE);
		register(Blacklist.INSTANCE);
		register(Quote.INSTANCE);
		// 服务类
		register(Help.INSTANCE);
		register(Version.INSTANCE);
		register(ShowAdmin.INSTANCE);
		register(Echo.INSTANCE);
		register(ExecuteInfo.INSTANCE);
		register(Execute.INSTANCE);
		// 娱乐类
		register(Wolfram.INSTANCE);
		register(Hitokoto.INSTANCE);
		register(Mo.INSTANCE);
		register(AnswerMe.INSTANCE);
	}

	GroupMessageResponder getGroupResponderByKeywordRegex(String keyword) {
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
		long groupUid = message.getGroupUid();
		String sender = message.getSenderNickName();
		long senderUid = message.getSenderUid();

		avalon.util.GroupConfig groupConfig = GroupConfig.instance().getConfig(groupUid);
		if (groupConfig == null) {
			LOGGER.warn("listened message from not configured group " +
					groupUid + " . Ignored this message. Please config this group in `.\\group.json`.");
			return;
		}
		if (groupConfig.isRecord() && !Constants.Basic.DEBUG)
			Recorder.getInstance().recodeGroupMessage(message);
		if (!groupConfig.isListen())
			return;
		if (ArrayUtils.contains(groupConfig.getBlacklist(), senderUid))
			return;

		LongStream adminUidStream = Arrays.stream(groupConfig.getAdmin());
		boolean admin = adminUidStream.anyMatch(e -> e == senderUid);

		if (Constants.Setting.Block_Words_Punishment_Mode_Enabled)
			if (publishPeopleMap.containsKey(senderUid)) {
				if (publishPeopleMap.get(senderUid) >= punishFrequency) {
					LOGGER.info("Account " + senderUid + ":" + sender + " was blocked. Please entered " +
							"\"Avalon blacklist remove " + senderUid + "\" to the group " + groupUid + ":" +
							message.getGroupName() + " if you really want to unblock this account.");
					if (!admin)
						message.response(AT(message) + " 您的帐号由于发送过多不允许关键字，现已被屏蔽~o(╯□╰)o！");
					return;
				}
			} else
				publishPeopleMap.put(senderUid, 0);

		for (Map.Entry<Pattern, GroupMessageResponder> patternAPI : apiList.entrySet()) {
			GroupMessageResponder value = patternAPI.getValue();
			ResponderInfo info = value.responderInfo();

			if (patternCheck(patternAPI.getKey(), message)) {
				if (!APISurvivePool.getInstance().isSurvive(value)) {
					if (!APISurvivePool.getInstance().isNoticed(value)) {
						if (!info.getKeyWordRegex().matcher("+1s").find())
							message.response(AT(message) + " 对不起，您调用的指令响应器目前已被停止；注意：此消息仅会显示一次。");
						APISurvivePool.getInstance().setNoticed(value);
					}
				} else if (MessageChecker.check(message) &&
						isResponderEnable(value) &&
						permissionCheck(info.getPermission(), groupConfig, message))
					value.doPost(message, groupConfig);
				return;
			}
		}

		for (Map.Entry<Pattern, CustomGroupResponder> patternAPIEntry : customApiList.entrySet()) {
			CustomGroupResponder value = patternAPIEntry.getValue();
			if (patternCheck(patternAPIEntry.getKey(), message)) {
				if (MessageChecker.check(message))
					value.doPost(message, groupConfig);
				return;
			}
		}
	}

	private boolean permissionCheck(ResponderPermission permission, avalon.util.GroupConfig config, GroupMessage message) {
		long senderUid = message.getSenderUid();
		boolean result = false;

		if (senderUid == DEBUG_MESSAGE_UID)
			return true;

		if (permission == ResponderPermission.ADMIN)
			result = ArrayUtils.contains(config.getAdmin(), senderUid);
		else if (permission == ResponderPermission.OWNER)
			result = config.getOwner() == senderUid;
		else if (permission == ResponderPermission.ALL)
			result = true;

		if (!result)
			message.response(AT(message) + " 致命错误：需要`sudo`以执行此操作！（雾");
		return result;
	}

	private boolean patternCheck(Pattern key, GroupMessage groupMessage) {
		String lowerContent = groupMessage.getContent().toLowerCase();
		long time = groupMessage.getTimeLong();
		if (!key.matcher(lowerContent).find())
			return false;
		// 屏蔽词判断
		for (String thisBlockString : blockWordList)
			if (groupMessage.getContent().
					trim().
					toLowerCase().
					replaceAll("[\\pP\\p{Punct}]", "").contains(thisBlockString)) {
				String notice = "您发送的消息含有不允许的关键词！";
				if (Constants.Setting.Block_Words_Punishment_Mode_Enabled) {
					notice = "您发送的消息含有不允许的关键词，注意：" + punishFrequency +
							"次发送不允许关键词后帐号将被屏蔽！⊙﹏⊙!";
					blackListPlus(groupMessage.getSenderUid());
				}
				groupMessage.response(AT(groupMessage) + " " + notice);
				return false;
			}
		// 冷却判断
		if (!cooling.trySet(time)) {
			if (!Variables.Limit_Noticed) {
				if (key.matcher("+1s").find())
					return false;
				groupMessage.response(
						String.format(
								"%s 对不起，您的指令超频。%dms内仅能有一次指令输入，未到%dms内的输入将被忽略。注意：此消息仅会显示一次。",
								AT(groupMessage),
								coolingDuration,
								coolingDuration
						));
				Variables.Limit_Noticed = true;
			}
			LOGGER.info(
					String.format("cooling blocked message %d sent by %d in %s.",
							groupMessage.getId(), groupMessage.getSenderUid(), groupMessage.getGroupName()));
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		System.setProperty("file.encoding", "UTF-8");

		// FIXME 感觉可以删掉，因为是自动加载的
		Config.Companion.instance();
		RunningData.getInstance();
		new Constants.Basic();
		new Constants.Address();
		AvalonPluginPool.INSTANCE.load();
		if (!Constants.Basic.DEBUG) {
			System.err.println("Debug not on! Exiting...");
			return;
		}
		Scanner scanner = new Scanner(System.in);
		int id = 0;
		//noinspection InfiniteLoopStatement
		while (true) {
			System.out.print("Input here:");
			String content = scanner.nextLine();
			GroupMessage message = new GroupMessage(++id, System.currentTimeMillis(),
					DEBUG_MESSAGE_UID, "Test", 617118724, "Test Group", content);
			GroupMessageHandler.getInstance().handle(message);
			System.out.println("===");
		}
	}

	private void blackListPlus(long senderUid) {
		int pastValue = publishPeopleMap.get(senderUid);
		publishPeopleMap.put(senderUid, ++pastValue);
	}

	public static void addGroupMessageResponder(GroupMessageResponder responder) {
		apiList.put(responder.responderInfo().getKeyWordRegex(), responder);
	}

	public static void addCustomGroupResponder(CustomGroupResponder responder) {
		customApiList.put(responder.getKeyWordRegex(), responder);
	}

	static Map<Pattern, CustomGroupResponder> getCustomApiList() {
		return customApiList;
	}

	static int getPunishFrequency() {
		return punishFrequency;
	}

	static Map<Long, Integer> getSetBlackListPeopleMap() {
		return publishPeopleMap;
	}
}
