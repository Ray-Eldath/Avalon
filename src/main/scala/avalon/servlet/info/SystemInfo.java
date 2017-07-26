package avalon.servlet.info;

import avalon.tool.pool.ConstantPool;
import com.sun.management.OperatingSystemMXBean;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eldath Ray on 2017/4/19 0019.
 * <p>
 * Usage: ../avalon/v0/info/get_system_info
 *
 * @author Eldath Ray
 */
public class SystemInfo extends HttpServlet {
    private static double cpuLoad, loadAverage;
    private static String name, arch, version;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject object = new JSONObject();
        // memory
        JSONObject memory = new JSONObject();
        long[] memoryLong = getMemory();
        memory.put("unit", "MB");
        memory.put("total", memoryLong[0]);
        memory.put("free", memoryLong[1]);
        memory.put("rate", memoryLong[2]);
        object.put("memory", memory);
        // disk
        JSONObject disk = new JSONObject();
        HashMap<String, HashMap<String, Long>> diskMap = getDisk();
        JSONObject thisDisk;
        for (Map.Entry<String, HashMap<String, Long>> entry : diskMap.entrySet()) {
            thisDisk = new JSONObject();
            String key = entry.getKey();
            HashMap<String, Long> value = entry.getValue();
            for (Map.Entry<String, Long> values : value.entrySet())
                thisDisk.put(values.getKey(), values.getValue());
            disk.put(key, thisDisk);
        }
        object.put("disk", disk);
        // CPU
        object.put("cpu", cpuLoad);
        // other
        object.put("load_average", loadAverage);
        // operating system
        JSONObject operatingSystem = new JSONObject();
        operatingSystem.put("start_time", ConstantPool.Basic.startTime);
        operatingSystem.put("runtime", System.currentTimeMillis() - ConstantPool.Basic.startTime);
        operatingSystem.put("name", name);
        operatingSystem.put("arch", arch);
        operatingSystem.put("version", version);
        object.put("operating_system", operatingSystem);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().print(object.toString());
    }

    private long[] getMemory() {
        OperatingSystemMXBean bean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        loadAverage = bean.getSystemLoadAverage();
        cpuLoad = bean.getSystemCpuLoad();
        name = bean.getName();
        arch = bean.getArch();
        version = bean.getVersion();
        long[] memory = new long[3];
        // 总的物理内存+虚拟内存
        memory[0] = Math.round(((double) bean.getTotalPhysicalMemorySize() / (1024 * 1024) * 100 / 100.0));
        // 剩余的物理内存
        memory[1] = Math.round(((double) bean.getFreePhysicalMemorySize() / (1024 * 1024) * 100 / 100.0));
        // 比率
        memory[2] = (long) (1 - memory[0] * 1.0 / memory[1]) * 100;
        return memory;
    }

    private static HashMap<String, HashMap<String, Long>> getDisk() {
        HashMap<String, HashMap<String, Long>> disk = new HashMap<>();
        File[] roots = File.listRoots();// 获取磁盘分区列表
        HashMap<String, Long> thisDisk = new HashMap<>();
        for (File file : roots) {
            thisDisk.clear();
            long totalSpace = file.getTotalSpace();
            long usableSpace = file.getUsableSpace();
            if (totalSpace > 0) {
                thisDisk.put("total", Math.round(((double) totalSpace / (1024 * 1024)) * 100 / 100.0));
                thisDisk.put("free", Math.round((((double) usableSpace / (1024 * 1024)) * 100) / 100.0));
            }
            disk.put(file.getPath(), thisDisk);
        }
        return disk;
    }
}
