package main;

import util.Message;

import java.io.UnsupportedEncodingException;

/**
 * Created by Eldath Ray on 2017/3/28.
 *
 * @author Eldath Ray
 */
class MessageChecker {
    static boolean checkEncode(Message message) {
        String content = message.getContent().toLowerCase();
        try {
            if (!content.equals(new String(content.getBytes("GB2312"), "GB2312"))) {
                message.response("@" + message.getSenderNickName() + " 您的指示编码好像不对劲啊╮(╯_╰)╭");
                return false;
            }
        } catch (UnsupportedEncodingException ignore) {
        }
        return true;
    }
}
