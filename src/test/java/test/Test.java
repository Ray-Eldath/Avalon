package test;

import avalon.group.GroupMessageHandler;
import avalon.util.GroupMessage;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class Test {
    public static void main(String[] args) throws Exception {
        String content = "avalon execute cpp11\n" +
                "#include <cstdio>\n" +
                "int array[]={1,2,3};\n" +
                "int main(){\n" +
                "  for(int a : array)\n" +
                "     printf(\"%d\", a);\n" +
                "return 0;\n" +
                "}";
//        content = "avalon execute py\n" +
//                "print(12376*567/456456)";
        GroupMessageHandler.getInstance().handle(
                new GroupMessage(0, System.currentTimeMillis(), 1464443139,
                        "Ray Eldath", 617118724, "QQ聊天机器人测试讨论", content));
    }
}
