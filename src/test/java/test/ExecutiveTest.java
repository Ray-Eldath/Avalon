package test;

import avalon.extend.Executive;
import avalon.tool.pool.ExtendLanguagePool;

import java.util.Map;

/**
 * Created by Eldath Ray on 2017/6/24 0024.
 *
 * @author Eldath Ray
 */
public class ExecutiveTest {

    public static void main(String[] args) throws Exception {
        Map<String, Object> result = Executive.execute(ExtendLanguagePool.get("py"), "print 1+1");
        for (Map.Entry<String, Object> entry : result.entrySet())
            System.out.println(entry.getKey() + ":" + entry.getValue());
    }
}
