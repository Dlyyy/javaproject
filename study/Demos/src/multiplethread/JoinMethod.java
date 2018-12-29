package multiplethread;

import character.Hero2;
/*
首先解释一下主线程的概念
        所有进程，至少会有一个线程即主线程，即main方法开始执行，就会有一个看不见的主线程存在。
        在45行执行t.join，即表明在主线程中加入该线程。
        主线程会等待该线程结束完毕， 才会往下运行。*/

public class JoinMethod {
    public static void main(String[] args) {
        final Hero2 gareen = new Hero2();
        gareen.name = "盖伦";
        gareen.hp = 616;
        gareen.damage = 50;

        final Hero2 teemo = new Hero2();
        teemo.name = "提莫";
        teemo.hp = 300;
        teemo.damage = 30;

        final Hero2 bh = new Hero2();
        bh.name = "赏金猎人";
        bh.hp = 500;
        bh.damage = 65;

        final Hero2 leesin = new Hero2();
        leesin.name = "盲僧";
        leesin.hp = 455;
        leesin.damage = 80;

        Thread t1= new Thread(){
            public void run(){
                while(!teemo.isDead()){
                    gareen.attackHero(teemo);
                }
            }
        };

        t1.start();

        //代码执行到这里，一直是main线程在运行
        try {
            //t1线程加入到main线程中来，只有t1线程运行结束，才会继续往下走
            t1.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Thread t2= new Thread(){
            public void run(){
                while(!leesin.isDead()){
                    bh.attackHero(leesin);
                }
            }
        };
        //会观察到盖伦把提莫杀掉后，才运行t2线程
        t2.start();
    }
}
