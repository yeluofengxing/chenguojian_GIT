package cn.awakening;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

//自旋锁（好处不用加锁，不阻塞线程，坏处，循环CPU消耗高）
public class spinLockDemo {
    private static AtomicReference<Thread> atomicReference  = new AtomicReference();

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                myLock();
                System.out.println(Thread.currentThread().getName()+"开始干活");
                try{
                    TimeUnit.SECONDS.sleep(5);
                }catch(Exception e) {
                    e.printStackTrace();
                }
                myUnLock();
            }
        },"AA").start();

        try{
            TimeUnit.SECONDS.sleep(1);
        }catch(Exception e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                myLock();
                System.out.println(Thread.currentThread().getName()+"开始干活");
                try{
                    TimeUnit.SECONDS.sleep(5);
                }catch(Exception e) {
                    e.printStackTrace();
                }
                myUnLock();
            }
        },"BB").start();
    }
    public static void myLock(){
        Thread thread = Thread.currentThread();
        System.out.println("线程"+thread.getName()+" 进来了");
        while (!atomicReference.compareAndSet(null,thread)){

        }
        System.out.println("线程"+thread.getName()+" lock");
    }
    public  static void myUnLock () {
        Thread thread = Thread.currentThread();
        atomicReference.compareAndSet(thread,null);
        System.out.println("线程"+thread.getName()+" UnLock");
    }
}
