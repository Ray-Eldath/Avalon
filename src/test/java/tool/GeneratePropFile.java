package tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eldath on 2017/1/30 0030+0800.
 *
 * @author Eldath
 */
public class GeneratePropFile {
    private static Map<String, String> schedule = new HashMap<>();
    private static final String nowYear = String.valueOf(LocalDate.now().getYear());

    static {
        schedule.put("04-28T14:19:17", "「Hello, World.」");
        schedule.put("02-08T06:06:06", "「时至今日，遍布世界各地大大小小的计算机都仍然遵循着冯·诺依曼的计算机基本结构，统称之为“冯·诺依曼机器。」 - 1957年2月8日，计算机之父『冯•诺依曼』逝世。");
        schedule.put("01-29T19:03:54", "「Avalon已上线。」 - 2017年1月29日Avalon第一次上线测试");
        schedule.put("09-01T11:59:59", "「Keep Calm and Carry On 保持冷静，继续前行」 - 英国皇家政府在第二次世界大战时期为鼓舞民众的海报");
        schedule.put("10-12T18:42:14", "「丹尼斯·里奇，那个给乔布斯提供肩膀的巨人」 - 关于2011年10月12日C语言之父逝世的评论");
        schedule.put("04-28T03:27:27", "「有些事实被认知为真，但不必然可证。」 - 1906年4月28日，编程语言之父库尔特·弗雷德里希·哥德尔降生。");
        schedule.put("10-30T07:59:59", "「记忆最深的，你上堂讲课没有带书，没有带讲义，全部在你的脑海里，而且我们还跟不上，这一点实在了不起。」");
        schedule.put("07-28T13:17:50", "「广大军民，努力地奋战，与洪水搏斗。我们的军队要发扬不怕疲劳，不怕艰险，连续作战的精神。」 - 江泽民主席于1998年的长江抗洪讲话");
        schedule.put("08-31T15:02:11", "「 消える飛行機雲僕たちは見送った 」 - 初音未来《鸟之诗》");
        schedule.put("09-01T16:27:13", "「它被捕捉作食物以及养猪饲料，由棚车装载运向美国东部的城市；1805年，纽约一对（2只）鸽子的价钱为二分。」 - 9月1日，旅鸽灭绝");
        schedule.put("09-02T08:04:12", "「 下名ハ茲ニ日本帝國大本營竝ニ何レノ位置ニ在ルヲ問ハズ一切ノ日本國軍隊及日本國ノ支配下ニ在ル一切ノ軍隊ノ聯合國ニ對スル無條件降伏ヲ布告ス 」 - 1945年9月2日，日本签署『降伏文書』，宣布无条件投降。");
        schedule.put("08-30T03:27:27", "「苟利国家生死以，岂因祸福避趋之。」");
    }

    public static void main(String[] args) throws Exception {
        // Write part
        Path path = Paths.get(System.getProperty("user.dir") + "/ShowMsg.properties");
        System.out.println("At: " + path.toString());
        Files.deleteIfExists(path);
        Files.createFile(path);
        try (BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            bw.write("#This config file is generated by Avalon to config ShowMsg plugin.\n");
            for (Map.Entry stringEntryOnce : schedule.entrySet()) {
                String key = (String) stringEntryOnce.getKey();
                String value = (((String) stringEntryOnce.getValue())).replace("\n", "\\n");
                bw.write(key + "=" + value);
                bw.newLine();
            }
        }
        // Read part
        String thisLine;
        String[] split;
        HashMap<String, String> read = new HashMap<>();
        read.clear();
        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            while ((thisLine = br.readLine()) != null) {
                if (!thisLine.contains("=") || thisLine.charAt(0) == '#') continue;
                split = thisLine.split("=");
                read.put(split[0], split[1]);
            }
        }
        for (Map.Entry stringEntry : read.entrySet()) {
            String key = nowYear + "-" + stringEntry.getKey();
            String value = ((String) stringEntry.getValue()).replace("\\n", "\n");
            LocalDateTime ldt = LocalDateTime.parse(key);
            String nowTime = ldt.getYear() + "年" +
                    ldt.getMonthValue() + "月" +
                    ldt.getDayOfMonth() + "日 " +
                    ldt.getHour() + "时" +
                    ldt.getMinute() + "分" +
                    ldt.getSecond() + "秒";
            System.out.println("现在是" + nowTime + "\n" + value);
        }
    }
}