package avalon.api

import avalon.friend.FriendMessageResponder
import avalon.group.GroupMessageHandler
import avalon.group.GroupMessageResponder
import avalon.group.Mo
import avalon.group.ResponderInfo
import avalon.tool.pool.APISurvivePool
import avalon.tool.pool.Constants
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import avalon.util.Plugin
import java.util.regex.Pattern

object RegisterResponder {
    @JvmStatic
    private val map = hashMapOf<Plugin, ArrayList<CustomGroupResponder>>() // 存储每个Plugin持有的CustomGroupResponder

    @Suppress("MemberVisibilityCanBePrivate")
    @JvmStatic
    fun register(responder: GroupMessageResponder) {
        val responderHolder = GroupMessageResponderHolder(
                if (responder == Mo)
                    responder.responderInfo().keyWordRegex
                else
                    Pattern.compile(Constants.Basic.DEFAULT_REGEX_PREFIX.joinToString(separator = "|") +
                            responder.responderInfo().keyWordRegex.pattern()),
                responder)
        GroupMessageHandler.addGroupMessageResponder(responderHolder)
        APISurvivePool.getInstance().addAPI(responderHolder)
    }

    /**
     * 内部方法。被设计为仅在Avalon内部可调用以强制所有外部功能均可以在配置文件中关闭。
     */
    @JvmName(" {# THIS METHOD IS NOT DESIGNED FOR OUTER CALL #} ")
    @JvmSynthetic
    internal fun registerInner(responder: GroupMessageResponder, disableNotAllowed: Boolean = false) {
        register(responder)
        if (disableNotAllowed)
            GroupMessageHandler.setDisabledNotAllowed(responder)
    }

    @JvmStatic
    fun register(plugin: Plugin, responder: CustomGroupResponder) {
        if (plugin in map) {
            val tempList = map[plugin]!!
            tempList.add(responder)
            map.replace(plugin, tempList)
        } else {
            val tempList = arrayListOf<CustomGroupResponder>()
            tempList.add(responder)
            map[plugin] = tempList
        }
        GroupMessageHandler.addCustomGroupResponder(responder)
    }

    @JvmStatic
    fun register(responder: FriendMessageResponder): Nothing =
            throw UnsupportedOperationException("register for FriendMessageResponder not finish yet :~)")

    @JvmStatic
    fun queryAvalonPlugin(plugin: Plugin): ArrayList<CustomGroupResponder> = map[plugin]!!
}

class GroupMessageResponderHolder(val regex: Pattern, val responder: GroupMessageResponder) {
    fun doPost(message: GroupMessage, groupConfig: GroupConfig) = responder.doPost(message, groupConfig)

    fun instance(): GroupMessageResponder? = responder

    fun responderInfo(): ResponderInfo = ResponderInfo(
            responder.responderInfo().helpMessage,
            regex,
            responder.responderInfo().configIdentifier,
            responder.responderInfo().manageable,
            responder.responderInfo().permission)

    val name = responder.javaClass.simpleName
}