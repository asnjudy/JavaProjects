package thread.lock;

/**
 * Created by asnju on 2016/11/5.
 */
public class MyThread extends Thread {

    private MyService myService;

    public MyThread(MyService myService) {
        this.myService = myService;
    }

    @Override
    public void run() {
        myService.testMethod();
        myService.testMethod();
        myService.testMethod();
    }


    public static void main(String[] args) {

        MyService service = new MyService();

        MyThread thread = new MyThread(service);
        MyThread thread2 = new MyThread(service);


        thread.start();
        thread2.start();
    }
}
