package avalon.extend

import java.util

import avalon.model.executive.ExecutiveLanguage


/**
  * Created by Eldath Ray on 2017/6/24 0024.
  *
  * @author Eldath Ray
  */
object ExecutiveLanguagePool {
  private val map = new util.HashMap[String, ExecutiveLanguage](10, 3)

  map.put("cpp11",
    new ExecutiveLanguage("cpp11", "g++ -O2 -Wall -std=c++11 -o {filename}.exe {filename}.cpp -lm", "{filename}.exe"))
  map.put("c99",
    new ExecutiveLanguage("cpp", "g++ -O2 -Wall -std=c99 -o {filename}.exe {filename}.cpp -lm", "{filename}.exe"))
  map.put("py",
    new ExecutiveLanguage("py", "python -m py_compile {filename}.py", "python {filename}.py"))
  map.put("java",
    new ExecutiveLanguage("java", "javac {filename}.java", "java -cp {filename}"))

  def get(key: String): ExecutiveLanguage = map.get(key.toLowerCase)
}