package avalon.group

import java.util.Random
import java.util.regex.Pattern

import avalon.tool.pool.VariablePool
import avalon.util.GroupMessage

/**
  * Created by Eldath on 2017/1/28 0028.
  *
  * @author Eldath
  */
object Mo extends GroupMessageResponder{
  override def doPost(message: GroupMessage): Unit = {
    if (VariablePool.Mo_Reach_Max) return
    if (VariablePool.Mo_Count >= 50) {
      message.response("哼！你们今天膜的太多啦！长者肯定会生气的！")
      VariablePool.Mo_Reach_Max = true
      return
    }
    val responseMessages = Array(
      "哈哈蛤哈",
      "你们有没有... ...就是那种... ...那种... ...诗？",
      "那首诗怎么念来着？苟利国家... ...",
      "我跟你江，你们这是要负泽任的，民白不？",
      "还是去弹夏威夷吉他吧！",
      "枸杞有养生功效，古人云：枸利果佳生食宜，气阴火服必驱之",
      "下面我们有请州长夫人演唱！")
    message.response(responseMessages(new Random().nextInt(responseMessages.length)))
    VariablePool.Mo_Count += 1
  }

  override def getHelpMessage = "膜*关键词：随机触发膜*语句"

  override def getKeyWordRegex: Pattern = Pattern.compile("\\w?(\\+1s|-1s|膜蛤|苟|续命|州长夫人)\\w?")

  override def instance: GroupMessageResponder = this
}