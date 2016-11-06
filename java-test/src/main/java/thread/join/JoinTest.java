package thread.join;



class MyThread extends Thread {

    @Override
    public void run() {

        System.out.println(Thread.currentThread().getName() + " begin time " + System.currentTimeMillis());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " end time " + System.currentTimeMillis());
    }
}


public class JoinTest {


    public static void main(String[] args) throws InterruptedException {

        MyThread myThread = new MyThread();
        myThread.setDaemon(true);
        myThread.start();

        myThread.join(2000);
        System.out.println(Thread.currentThread().getName() + " time " + System.currentTimeMillis());
    }
}
