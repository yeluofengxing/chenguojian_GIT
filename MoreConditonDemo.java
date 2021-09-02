package cn.awakening;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
//多线程之间按顺序调用，实现A-B-C 三个线程启动，要求如下：
//A打印5次，b打印10次，c打印15次 来10轮

public class MoreConditonDemo {
    private int count = 1;
    ReentrantLock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    Condition condition2 = lock.newCondition();
    Condition condition3 = lock.newCondition();

    public void print(int number) {
        lock.lock();
        try{
            if (number == 1) {
                if (count!=1) {
                    condition.await();
                }
                for (int i=1;i<=5;i++){
                    System.out.println(Thread.currentThread().getName()+":"+ i);
                }
                count =2;
                condition2.signal();
            } else if (number == 2) {
                if (count!=2) {
                    condition2.await();
                }
                for (int i=1;i<=10;i++){
                    System.out.println(Thread.currentThread().getName()+":"+ i);
                }
                count = 3;
                condition3.signal();
            } else if (number == 3){
                if (count!=3) {
                    condition3.await();
                }
                for (int i=1;i<=15;i++){
                    System.out.println(Thread.currentThread().getName()+":"+ i);
                }
                count=1;
                condition.signal();
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        MoreConditonDemo demo = new MoreConditonDemo();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i =1;i<=5;i++){
                    demo.print(1);
                }
            }
        },"A").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i =1;i<=10;i++){
                    demo.print(2);
                }
            }
        },"B").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i =1;i<=15;i++){
                    demo.print(3);
                }
            }
        },"C").start();
    }
}
