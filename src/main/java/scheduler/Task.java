package scheduler;

/**
 * Created by Eldath on 2017/1/31 0031.
 *
 * @author Eldath
 */
public interface Task extends Runnable {
    @Override
    void run();

    @Override
    String toString();
}
