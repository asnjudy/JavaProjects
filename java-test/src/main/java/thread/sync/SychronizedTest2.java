package thread.sync;



class MyService {

    private boolean isContinueRun = true;

    synchronized public void methodA() {
        System.out.println("enter methodA");

        while (isContinueRun) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("do");
        }

        System.out.println("exit methodA");

    }


    synchronized public void methodB() {
        System.out.println("enter methodB");
        System.out.println("exit methodB");
    }
}

public class SychronizedTest2 {

    public static void main(String[] args) throws InterruptedException {

        final MyService myService = new MyService();


        new Thread(new Runnable() {
            @Override
            public void run() {
                myService.methodA();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                myService.methodB();
            }
        }).start();


        Thread.sleep(100000);

    }
}
