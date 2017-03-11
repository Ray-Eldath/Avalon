package util;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.time.LocalDateTime;

/**
 * Created by Eldath on 2017/2/11 0011.
 *
 * @author Eldath
 */
public class GroupMessage implements Message {
    private static Logger logger = LoggerFactory.getLogger(GroupMessage.class);
    private int id;
    private LocalDateTime time;
    private long senderUid, groupUid;
    private String senderNickName, groupName, content;

    public GroupMessage(int id, LocalDateTime time, long senderUid, String senderNickName, long groupUid, String groupName, String content) {
        this.id = id;
        this.time = time;
        this.senderUid = senderUid;
        this.groupUid = groupUid;
        this.senderNickName = senderNickName;
        this.groupName = groupName;
        this.content = content;
    }

    @Override
    public void response(String reply) {
        response(reply, false, -1);
    }

    public void response(String reply, boolean shutUp, int shutUpTime) {
        try {
            JSONObject object = new JSONObject();
            object.put("reply", reply);
            // object.put("shutup", shutUp ? 1 : 0);
            if (shutUpTime != -1)
                object.put("shutup_time", shutUpTime);
            HttpURLConnection connection = (HttpURLConnection) new URL("http://127.0.0.1:8088").openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.connect();
            //
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(object.toString());
            out.flush();
            out.close();
            //
            System.out.println(new JSONTokener(connection.getInputStream()).toString());
            connection.disconnect();
        } catch (Exception e) {
            logger.warn("Exception thrown while response " + getSenderUid() + ":", e);
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public long getSenderUid() {
        return senderUid;
    }


    public long getGroupUid() {
        return groupUid;
    }

    @Override
    public String getSenderNickName() {
        return senderNickName;
    }

    public String getGroupName() {
        return groupName;
    }

    @Override
    public String getContent() {
        return content;
    }
}
