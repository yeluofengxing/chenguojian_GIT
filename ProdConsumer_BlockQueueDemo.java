package cn.awakening;

import com.mysql.cj.util.TimeUtil;
import lombok.SneakyThrows;

import javax.validation.constraints.Pattern;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class MyResource{
    private volatile Boolean FLAG = true; //开关
    private AtomicInteger cake = new AtomicInteger();
    private BlockingQueue<String>  queue = null;

    // 构造方法
    public MyResource(BlockingQueue<String> deque) {
        this.queue = deque;
    }
    //生产
    public void myProd() throws InterruptedException {
        System.out.println("生产线程"+Thread.currentThread().getName()+"启动");
        String data = null;
        Boolean reVal = false;
        while(FLAG) {
            data = String.valueOf(cake.incrementAndGet());
            reVal= queue.offer(data,2L,TimeUnit.SECONDS);
            if(reVal) {
                System.out.println("生产蛋糕插入队列成功"+data);
            }
            TimeUnit.SECONDS.sleep(1L);
        }
        System.out.println("老板叫停了，生产停止");

    }
    //消费
    public void myConsumer() throws InterruptedException {
        System.out.println("消费线程"+Thread.currentThread().getName()+"启动");
        String data = null;
        while(FLAG) {
            data = queue.poll(2L, TimeUnit.SECONDS);  // 两秒钟取一次
            if(data == null||data.equalsIgnoreCase("")) {
                System.out.println("两秒钟都没有取到蛋糕，退出消费");
                FLAG =false;
                return;
            }
            System.out.println("消费蛋糕取出队列成功"+data);
        }
    }
    //退出方法
    public void stop() {
        this.FLAG = false;
    }
}

public class ProdConsumer_BlockQueueDemo {

    public static void main(String[] args) throws Exception {
        MyResource resource = new MyResource(new ArrayBlockingQueue<String>(5));
        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                resource.myProd();
            }
        },"A").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    resource.myConsumer();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"B").start();

        // 让生产消费先执行5秒钟再停止
        TimeUnit.SECONDS.sleep(5);
        resource.stop();
        System.out.println("大老板叫停了");
    }

}
