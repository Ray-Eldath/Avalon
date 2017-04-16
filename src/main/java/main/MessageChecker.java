package main;

import util.Message;

import java.io.UnsupportedEncodingException;

/**
 * Created by Eldath Ray on 2017/3/28.
 *
 * @author Eldath Ray
 */
public class MessageChecker {
    public static boolean checkEncode(Message message) {
        String content = message.getContent().toLowerCase();
        try {
            if (!content.equals(new String(content.getBytes("GB2312"), "GB2312"))) {
                message.response("@" + message.getSenderNickName() + " 您消息的编码好像不对劲啊╮(╯_╰)╭");
                return false;
            }
        } catch (UnsupportedEncodingException ignore) {
        }
        return true;
    }
}
