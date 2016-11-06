package thread.sync;



class Service {

    private boolean isContinueRun = true;


    public void runMethod() {

        String anyString = new String();

        while (isContinueRun) {
            synchronized (anyString) {

            }
        }
        System.out.println("停下来了！");
    }

    public void stopMethod() {
        isContinueRun = false;
    }

    public boolean isContinueRun() {
        return isContinueRun;
    }
}


public class SynchronizedVolatileTest {

    public static void main(String[] args) throws InterruptedException {


        final Service service = new Service();

        new Thread() {
            @Override
            public void run() {
                service.runMethod();
            }
        }.start();

        Thread.sleep(1000);

        new Thread() {
            @Override
            public void run() {
                service.stopMethod();
            }
        }.start();
        System.out.println("已发出停止命令");
        System.out.println("isContinueRun: " + service.isContinueRun());
    }
}
