import org.w3c.dom.css.Counter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final int PHILOSOPHER_VALUE = 5;
        CountDownLatch ctl = new CountDownLatch(PHILOSOPHER_VALUE);

        Lock[] forks = new Lock[PHILOSOPHER_VALUE];
        for (int i = 0; i < PHILOSOPHER_VALUE; i++) {
            forks[i] = new ReentrantLock();
        }

        Philosofer[] philosofers = new Philosofer[PHILOSOPHER_VALUE];
        Thread[] threads = new Thread[PHILOSOPHER_VALUE];

        for (int i = 0; i < PHILOSOPHER_VALUE; i++) {
            Lock leftFork = forks[i];
            Lock rightFork = forks[(i + 1) % PHILOSOPHER_VALUE];

            philosofers[i] = new Philosofer(i, leftFork, rightFork, ctl);
            threads[i] = new Thread(philosofers[i]);
            threads[i].start();
        }

        ctl.await();
        System.out.println("All philosofers finished.");
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}