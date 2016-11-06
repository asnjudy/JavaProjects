package thread.sync;


class Task {
    private int count;
    private int count2;


    public void increase() {
        synchronized (this) {
            count++;
            count2++;
            System.out.println(Thread.currentThread().getName() + ", count = " + count + ", count2 = " + count2);
        }
    }

    public void testMethodA() {

        synchronized (this) {
            count++;
            count2++;
            System.out.println("Sync 1: " + Thread.currentThread().getName() + ", count = " + count + ", count2 = " + count2);
        }

        System.out.println("        " + Thread.currentThread().getName() + ", " + System.currentTimeMillis());

        synchronized (this) {
            count++;
            count2++;
            System.out.println("Sync 2: " + Thread.currentThread().getName() + ", count = " + count + ", count2 = " + count2);
        }
    }


    public void testMethodB() {

        synchronized (this) {

            System.out.println("enter 1");

            // 该线程已获得锁，可进去；已经加锁，不用再加锁
            synchronized (this) {
                System.out.println("enter 2");
                synchronized (this) {
                    System.out.println("enter 3");
                    synchronized (this) {
                        System.out.println("xxxxxxxxx");
                    }
                    System.out.println("exit 3");
                }
                System.out.println("exit 2");
            }

            System.out.println("exit 1");
        }
    }
}

class MyThread extends Thread {

    private Task task;

    public MyThread(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        task.testMethodB();
    }
}


public class SynchronizedTest {

    public static void main(String[] args) {

        Task task = new Task();


        new MyThread(task).start();


    }
}
