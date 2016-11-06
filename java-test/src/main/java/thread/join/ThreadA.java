package thread.join;

/**
 * Created by asnju on 2016/11/5.
 */
public class ThreadA extends Thread {

    private Thread threadB;

    public ThreadA(Thread threadB) {
        this.threadB = threadB;
    }

    @Override
    public void run() {
        synchronized (threadB) {
            threadB.start();
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
