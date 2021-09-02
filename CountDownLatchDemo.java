package cn.awakening;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {
    private static CountDownLatch count = new CountDownLatch(5);

    public static void main(String[] args) {
        for (int i=1;i<6;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    count.countDown();
                    System.out.println(Thread.currentThread().getName()+"下班了，走人");
                }
            },String.valueOf(i)).start();
        }
        try {
            count.await(); // 计数器等于0才继续往下走
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("人都走位了，保洁阿姨关门");

    }
}
