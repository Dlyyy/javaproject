package multiplethread;

import java.awt.GradientPaint;

import character.Hero4;

/* 线程交互
故意设计减血线程频率更高，盖伦的血量迟早会到达1
        减血线程中使用while循环判断是否是1，如果是1就不停的循环,直到加血线程回复了血量
        这是不好的解决方式，因为会大量占用CPU,拖慢性能
        应使用wait和notify进行线程交互*/
public class InteractionDemo {
    public static void main(String[] args) {
        final Hero4 gareen = new Hero4();
        gareen.name = "盖伦";
        gareen.hp = 616;

        Thread t1 = new Thread(){
            public void run(){
                while(true){

                    //因为减血更快，所以盖伦的血量迟早会到达1
                    //使用while循环判断是否是1，如果是1就不停的循环
                    //直到加血线程回复了血量
                    while(gareen.hp==1){
                        continue;
                    }

                    gareen.hurt();
                    System.out.printf("t1 为%s 减血1点,减少血后，%s的血量是%.0f%n",gareen.name,gareen.name,gareen.hp);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        };
        t1.start();

        Thread t2 = new Thread(){
            public void run(){
                while(true){
                    gareen.recover();
                    System.out.printf("t2 为%s 回血1点,增加血后，%s的血量是%.0f%n",gareen.name,gareen.name,gareen.hp);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        };
        t2.start();
    }
}
