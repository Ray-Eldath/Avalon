package avalon.group

import java.util.regex.Pattern

import avalon.tool.Responder.AT
import avalon.tool.pool.ConstantPool.Basic.currentServlet
import avalon.util.GroupMessage

/**
  * Created by Eldath Ray on 2017/6/11 0011.
  *
  * @author Eldath Ray
  */
object ShowAdmin extends GroupMessageResponder {
  override def doPost(message: GroupMessage): Unit = {
    val adminUid = GroupMessageHandler.getAdminUid
    val builder = new StringBuilder(AT(message) + " 目前管理员有：\n")
    for (uid <- adminUid)
      builder.append(uid)
        .append("\t")
        .append(currentServlet.getGroupSenderNickname(message.getGroupUid, uid))//TODO 突然发现每个群可能管理员是不同的= =
    message.response(builder.toString)
  }

  override def getHelpMessage = "avalon whoisadmin|谁是管理员：显示管理员列表"

  override def getKeyWordRegex: Pattern = Pattern.compile("avalon whoisadmin|avalon 谁是管理员")

  override def instance: GroupMessageResponder = this
}