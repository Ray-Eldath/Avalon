package avalon.group;

import avalon.api.CustomGroupResponder;
import avalon.extend.Recorder;
import avalon.main.MessageChecker;
import avalon.tool.APIRateLimit;
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
import static avalon.tool.pool.Constants.Basic.debugMessageUid;

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

	private static final String[] blockWordList = toStringArray(Config.instance().getConfigArray("block_words"));
	private static final int punishFrequency = (int) Config.instance().get("block_words_punish_frequency");
	private static final APIRateLimit cooling = new APIRateLimit(3000L);

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
		// 服务类
		register(Help.getInstance());
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
		long groupUid = message.getGroupUid();
		String sender = message.getSenderNickName();
		long senderUid = message.getSenderUid();

		avalon.util.GroupConfig groupConfig = GroupConfig.instance().getConfig(groupUid);
		if (groupConfig == null) {
			LOGGER.warn("listened message from not configured group " +
					groupUid + " . Ignored this message. Please config this group in `.\\group.json`.");
			return;
		}
		if (groupConfig.isRecord() && !Constants.Basic.debug)
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
							"\"avalon blacklist remove " + senderUid + "\" to the group " + groupUid + ":" +
							message.getGroupName() + " if you really want to unblock this account.");
					if (!admin)
						message.response("@" + sender +
								" 您的帐号由于发送过多不允许关键字，现已被屏蔽~o(╯□╰)o！");
					return;
				}
			} else
				publishPeopleMap.put(senderUid, 0);

		for (Map.Entry<Pattern, GroupMessageResponder> patternAPIEntry : apiList.entrySet()) {
			GroupMessageResponder value = patternAPIEntry.getValue();
			if (doCheck(patternAPIEntry.getKey(), message)) {
				if (!APISurvivePool.getInstance().isSurvive(value)) {
					if (!APISurvivePool.getInstance().isNoticed(value)) {
						if (!value.getKeyWordRegex().matcher("+1s").find())
							message.response(AT(message) + " 对不起，您调用的指令响应器目前已被停止；" +
									"注意：此消息仅会显示一次。");
						APISurvivePool.getInstance().setNoticed(value);
					}
				} else if (MessageChecker.check(message) && isResponderEnable(value))
					value.doPost(message, groupConfig);
				return;
			}
		}

		for (Map.Entry<Pattern, CustomGroupResponder> patternAPIEntry : customApiList.entrySet()) {
			CustomGroupResponder value = patternAPIEntry.getValue();
			if (doCheck(patternAPIEntry.getKey(), message)) {
				if (MessageChecker.check(message))
					value.doPost(message, groupConfig);
				return;
			}
		}
	}

	private boolean doCheck(Pattern key, GroupMessage groupMessage) {
		String lowerContent = groupMessage.getContent().toLowerCase();
		long time = groupMessage.getTimeLong();
		if (!key.matcher(lowerContent).find())
			return false;
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
		if (!cooling.trySet(time)) {
			if (!Variables.Limit_Noticed) {
				if (key.matcher("+1s").find())
					return false;
				groupMessage.response(AT(groupMessage) +
						" 对不起，您的指令超频。3s内仅能有一次指令输入，未到3s内的输入将被忽略。注意：此消息仅会显示一次。");
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
		Config.instance();
		RunningData.getInstance();
		new Constants.Basic();
		new Constants.Address();
		AvalonPluginPool.INSTANCE.load();
		if (!Constants.Basic.debug) {
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
					debugMessageUid, "Test", 617118724, "Test Group", content);
			GroupMessageHandler.getInstance().handle(message);
			System.out.println("===");
		}
	}

	private void blackListPlus(long senderUid) {
		int pastValue = publishPeopleMap.get(senderUid);
		publishPeopleMap.put(senderUid, ++pastValue);
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

	static int getPunishFrequency() {
		return punishFrequency;
	}

	static Map<Long, Integer> getSetBlackListPeopleMap() {
		return publishPeopleMap;
	}
}
