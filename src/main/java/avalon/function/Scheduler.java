package avalon.function;

import avalon.function.task.Task;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Eldath on 2017/1/31 0031.
 *
 * @author Eldath
 */
public class Scheduler extends Thread {
	private static Map<LocalDate, Task> allTask = new TreeMap<>();

	static void scheduleTask(LocalDate runTime, Task doWhat) {
		allTask.put(runTime, doWhat);
	}

	@Override
	public void run() {
		if (allTask.isEmpty())
			return;
		LocalDate now = LocalDate.now();
		for (Map.Entry<LocalDate, Task> entry : allTask.entrySet()) {
			LocalDate date = entry.getKey();
			Task task = entry.getValue();
			if (now.getDayOfYear() == date.getDayOfYear()) {
				task.run();
				allTask.remove(date);
			}
		}
	}
}
