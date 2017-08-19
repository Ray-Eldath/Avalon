package test

import java.io.{File, IOException}

import avalon.extend.WolframGetter
import org.jdom2.JDOMException
import org.jdom2.input.SAXBuilder

/**
  * Created by Eldath Ray on 2017/6/11 0011.
  *
  * @author Eldath Ray
  */
object WolframTest {
  @throws[IOException]
  @throws[JDOMException]
  def main(args: Array[String]): Unit = {
    val builder = new SAXBuilder
    val pods = WolframGetter.get(builder.build(new File("F:/query.xml")).getRootElement)
    for (thisPod <- pods) {
      System.out.println(thisPod.getTitle)
      System.out.println(thisPod.getPlaintext)
      System.out.print("---")
    }
  }
}