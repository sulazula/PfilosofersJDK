import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;

public class Philosofer extends Thread {
    Random rand = new Random();

    private int id;
    public Lock leftFork;
    public Lock rightFork;
    private CountDownLatch ctl;
    private int count = 3;

    public Philosofer(int id, Lock leftFork, Lock rightFork, CountDownLatch ctl) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.ctl = ctl;
    }

    public void think() throws InterruptedException {
        System.out.println("Philosofer " + id + " think");
        Thread.sleep(ThreadLocalRandom.current().nextInt(3500, 4000));
    }

    public void eat() throws InterruptedException {
        System.out.println("Philosofer " + id + " eat");
        Thread.sleep(ThreadLocalRandom.current().nextInt(2500, 3000));
    }

    @Override
    public void run() {
        try {
            while (count > 0) {
                think();
                if (leftFork.tryLock()) {
                    try {
                        if (rightFork.tryLock()) {
                            try {
                                eat();
                                count--;
                                if (count == 0) {
                                    ctl.countDown();
                                    System.out.println("Philosofer " + id + " arent hungry yet");
                                }
                            } finally {
                                rightFork.unlock();
                            }
                        }
                    } finally {
                        leftFork.unlock();
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
