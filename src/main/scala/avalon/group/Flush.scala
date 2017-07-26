package avalon.group

import java.util.regex.Pattern

import avalon.extend.Recorder
import avalon.tool.Responder.AT
import avalon.util.GroupMessage

/**
  * Created by Eldath Ray on 2017/3/26 0026.
  *
  * @author Eldath Ray
  */
object Flush extends GroupMessageResponder{
  override def doPost(message: GroupMessage): Unit = {
    val senderUid = message.getSenderUid
    val admins = GroupMessageHandler.getAdminUid
    val sender = message.getSenderNickName
    for (thisAdmin <- admins) {
      if (senderUid == thisAdmin) {
        Recorder.getInstance.flushNow()
        System.gc()
        message.response("管理员：@" + sender + "缓存及临时文件刷新成功。")
        return
      }
    }
    message.response(AT(message) + "权限不足！")
  }

  override def getHelpMessage = "avalon flush：<管理员> 刷新缓存并清除临时文件"

  override def getKeyWordRegex: Pattern = Pattern.compile("avalon flush")

  override def instance: GroupMessageResponder = this
}