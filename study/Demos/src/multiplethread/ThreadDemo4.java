package multiplethread;

import character.Hero2;

public class ThreadDemo4 {
    public static void main(String[] args) {
        Hero2 gareen = new Hero2();
        gareen.name = "盖伦";
        gareen.hp = 616;
        gareen.damage = 50;

        Hero2 teemo = new Hero2();
        teemo.name = "提莫";
        teemo.hp = 300;
        teemo.damage = 30;

        Hero2 bh = new Hero2();
        bh.name = "赏金猎人";
        bh.hp = 500;
        bh.damage = 65;

        Hero2 leesin = new Hero2();
        leesin.name = "盲僧";
        leesin.hp = 455;
        leesin.damage = 80;

        //匿名类
        Thread t1= new Thread(){
            public void run(){
                //匿名类中用到外部的局部变量teemo，必须把teemo声明为final
                //但是在JDK7以后，就不是必须加final的了
                while(!teemo.isDead()){
                    gareen.attackHero(teemo);
                }
            }
        };

        t1.start();

        Thread t2= new Thread(){
            public void run(){
                while(!leesin.isDead()){
                    bh.attackHero(leesin);
                }
            }
        };
        t2.start();
    }
}
/*
使用匿名类，继承Thread,重写run方法，直接在run方法中写业务代码
        匿名类的一个好处是可以很方便的访问外部的局部变量。
        前提是外部的局部变量需要被声明为final。(JDK7以后就不需要了)*/
