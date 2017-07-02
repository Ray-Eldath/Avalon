package avalon.group

import avalon.util.BaseResponder
import avalon.util.GroupMessage
import java.util.regex.Pattern

/**
  * Created by Eldath on 2017/1/28 0028.
  *
  * @author Eldath
  */
trait GroupMessageResponder extends BaseResponder {
  def doPost(message: GroupMessage): Unit

  override def getHelpMessage: String

  override def getKeyWordRegex: Pattern

  override def equals(`object`: Any): Boolean =
    `object` != null &&
      `object`.isInstanceOf[GroupMessageResponder] &&
      getKeyWordRegex.toString == `object`.asInstanceOf[GroupMessageResponder].getKeyWordRegex.toString

  def instance: GroupMessageResponder
}