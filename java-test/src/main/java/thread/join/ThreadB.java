package thread.join;

/**
 * Created by asnju on 2016/11/5.
 */
public class ThreadB extends Thread {


    @Override
    public void run() {

        System.out.println(Thread.currentThread().getName() + " begin time = " + System.currentTimeMillis());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " end time = " + System.currentTimeMillis());
    }
}
