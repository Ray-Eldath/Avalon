package avalon.tool.pool

import java.util

import avalon.model.ExtendLanguage

/**
  * Created by Eldath Ray on 2017/6/24 0024.
  *
  * @author Eldath Ray
  */
object ExtendLanguagePool {
  private val map = new util.HashMap[String, ExtendLanguage](10, 3)

  map.put("cpp11", new ExtendLanguage("cpp11", "g++ -O2 -Wall -std=c++11 -o {filename}.exe {filename}.cpp -lm", "{filename}.exe"))
  map.put("c99", new ExtendLanguage("cpp", "g++ -O2 -Wall -std=c99 -o {filename}.exe {filename}.cpp -lm", "{filename}.exe"))
  map.put("py", new ExtendLanguage("py", "python -m py_compile {filename}.py", "python {filename}.py"))
  map.put("java", new ExtendLanguage("java", "javac {filename}.java", "java -cp {filename}"))

  def get(key: String): ExtendLanguage = map.get(key.toLowerCase)
}