package avalon.group

import java.util.regex.Pattern

import avalon.tool.Responder
import avalon.tool.Responder.AT
import avalon.tool.pool.ConstantPool
import avalon.util.GroupMessage
import org.apache.commons.lang3.StringUtils

/**
  * Created by Eldath on 2017/1/29 0029.
  *
  * @author Eldath
  */
object AnswerMe extends GroupMessageResponder{

  override def doPost(message: GroupMessage): Unit = {
    if (!ConstantPool.Setting.AnswerMe_Enabled) return
    var content = message.getContent.trim.toLowerCase.replaceAll("[\\pP\\p{Punct}]", "")
    var text = content
    text = text.replaceAll(getKeyWordRegex.toString, "")
    if ("" == text.replace(" ", "")) {
      message.response(AT(message) + " 消息不能为空哦~(*∩_∩*)")
      return
    }
    if (StringUtils.isAlpha(text)) if (text.length < 5) {
      message.response(AT(message) + " 您的消息过短~o(╯□╰)o！")
      return
    }
    else if (text.length < 3) {
      message.response(AT(message) + " 您的消息过短~o(╯□╰)o！")
      return
    }
    content = content.replaceAll(getKeyWordRegex.toString, "")
    val responseXiaoIce = Responder.responseXiaoIce(content)
    if (responseXiaoIce == null)
      return
    message.response(AT(message) + " " + responseXiaoIce)
  }

  override def getHelpMessage = "avalon answer me | 阿瓦隆回答我：激活智能回复功能"

  override def getKeyWordRegex: Pattern = Pattern.compile("avalon answer me |阿瓦隆回答我 ")

  override def instance: GroupMessageResponder = this
}