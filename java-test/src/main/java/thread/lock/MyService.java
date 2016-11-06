package thread.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by asnju on 2016/11/5.
 */
public class MyService {

    private Lock lock = new ReentrantLock();

    private static int count = 0;

    public void testMethod() {

        lock.lock();

        synchronized (this) {
            count++;
        }

        System.out.println("Begin ThreadName = " + Thread.currentThread().getName() + " time = " + System.currentTimeMillis() + " count = " + count);



        for (int i = 0; i < 5; i++) {

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("ThreadName = " + Thread.currentThread().getName() + " i = " + (i + 1) + " count = " + count);
        }

        System.out.println("End ThreadName = " + Thread.currentThread().getName() + " time = " + System.currentTimeMillis() + " count = " + count);
        lock.unlock();
    }
}
