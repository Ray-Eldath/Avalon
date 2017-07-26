package avalon.main

import avalon.util.Message

/**
  * Created by Eldath Ray on 2017/3/28.
  *
  * @author Eldath Ray
  */
object MessageChecker {
  def check(message: Message): Boolean = checkEncode(message)

  private def checkEncode(message: Message): Boolean = {
    val content = message.getContent
    if (!(content == new String(content.getBytes("GB2312"), "GB2312"))) {
      message.response("@" + message.getSenderNickName + " 您消息的编码好像不对劲啊╮(╯_╰)╭")
      return false
    }
    true
  }
}