package avalon.tool;

import java.util.ArrayList;

/**
 * Created by Eldath Ray on 2017/3/18 0018.
 *
 * @author peiyuxin
 */
class JSONFormatter {

    /**
     * json字符串的格式化
     *
     * @param json 需要格式的json串
     */
    static String format(String json) {
        if (json == null || json.trim().length() == 0)
            return null;
        int fixedLength = 0;
        ArrayList<String> tokenList = new ArrayList<>();
        String jsonTemp = json;
        //预读取
        while (jsonTemp.length() > 0) {
            String token = getToken(jsonTemp);
            jsonTemp = jsonTemp.substring(token.length());
            token = token.trim();
            tokenList.add(token);
        }
        for (int i = 0; i < tokenList.size(); i++) {
            String token = tokenList.get(i);
            int length = token.getBytes().length;
            if (length > fixedLength && i < tokenList.size() - 1 && tokenList.get(i + 1).equals(":"))
                fixedLength = length;
        }
        StringBuilder buf = new StringBuilder();
        int count = 0;
        for (int i = 0; i < tokenList.size(); i++) {
            String token = tokenList.get(i);
            if (token.equals(",")) {
                buf.append(token);
                doFill(buf, count, "   ");
                continue;
            }
            if (token.equals(":")) {
                buf.append(" ").append(token).append(" ");
                continue;
            }
            if (token.equals("{")) {
                String nextToken = tokenList.get(i + 1);
                if (nextToken.equals("}")) {
                    i++;
                    buf.append("{ }");
                } else {
                    count++;
                    buf.append(token);
                    doFill(buf, count, "   ");
                }
                continue;
            }
            if (token.equals("}")) {
                count--;
                doFill(buf, count, "   ");
                buf.append(token);
                continue;
            }
            if (token.equals("[")) {
                String nextToken = tokenList.get(i + 1);
                if (nextToken.equals("]")) {
                    i++;
                    buf.append("[ ]");
                } else {
                    count++;
                    buf.append(token);
                    doFill(buf, count, "   ");
                }
                continue;
            }
            if (token.equals("]")) {
                count--;
                doFill(buf, count, "   ");
                buf.append(token);
                continue;
            }

            buf.append(token);
            //左对齐
            if (i < tokenList.size() - 1 && tokenList.get(i + 1).equals(":")) {
                int fillLength = fixedLength - token.getBytes().length;
                if (fillLength > 0)
                    for (int j = 0; j < fillLength; j++)
                        buf.append(" ");
            }
        }
        return buf.toString();
    }

    private static String getToken(String json) {
        StringBuilder buf = new StringBuilder();
        boolean isInYinHao = false;
        while (json.length() > 0) {
            String token = json.substring(0, 1);
            json = json.substring(1);

            if (!isInYinHao &&
                    (token.equals(":") || token.equals("{") || token.equals("}")
                            || token.equals("[") || token.equals("]")
                            || token.equals(","))) {
                if (buf.toString().trim().length() == 0)
                    buf.append(token);
                break;
            }

            if (token.equals("\\")) {
                buf.append(token);
                buf.append(json.substring(0, 1));
                json = json.substring(1);
                continue;
            }
            if (token.equals("\"")) {
                buf.append(token);
                if (isInYinHao)
                    break;
                else {
                    isInYinHao = true;
                    continue;
                }
            }
            buf.append(token);
        }
        return buf.toString();
    }

    private static void doFill(StringBuilder buf, int count, String fillStringUnit) {
        buf.append("\n");
        for (int i = 0; i < count; i++)
            buf.append(fillStringUnit);
    }
}
