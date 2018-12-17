package modelo;

import java.util.concurrent.CountDownLatch;

public enum Lock {
    INSTANCE;

    private CountDownLatch lock;

    Lock() {
    }

    public void createLock(){
        lock = new CountDownLatch(1);
    }

    public void releaseLock(){
        if (lock != null)
            lock.countDown();
    }

    public CountDownLatch getLock() {
        return lock;
    }

    public void waitLock(){
        try {
            lock.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}