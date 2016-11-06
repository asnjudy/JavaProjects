package thread.sync;


import java.util.concurrent.atomic.AtomicInteger;

class Run implements Runnable {

    private boolean isContinueRun = true;
    private AtomicInteger count = new AtomicInteger();

    @Override
    public void run() {
        runMethod();
    }

    public void runMethod() {
        while (isContinueRun) {
            System.out.println("count = " + count.addAndGet(1));
        }
        System.out.println("停下来了！");
    }

    public boolean isContinueRun() {
        return isContinueRun;
    }

    public void setContinueRun(boolean continueRun) {
        isContinueRun = continueRun;
    }
}

public class ThreadTest {

    public static void main(String[] args) throws InterruptedException {

        Run run = new Run();
        new Thread(run).start();

        Thread.sleep(3);

        run.setContinueRun(false);
        System.out.println("已发出停止命令");
    }
}
