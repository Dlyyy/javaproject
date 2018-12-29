package multiplethread;

import character.Hero2;

public class ThreadDemo3 {
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

        Battle battle1 = new Battle(gareen,teemo);

        new Thread(battle1).start();

        Battle battle2 = new Battle(bh,leesin);
        new Thread(battle2).start();

    }
}
/*创建类Battle，实现Runnable接口
        启动的时候，首先创建一个Battle对象，然后再根据该battle对象创建一个线程对象，并启动

        Battle battle1 = new Battle(gareen,teemo);
        new Thread(battle1).start();


        battle1 对象实现了Runnable接口，所以有run方法，但是直接调用run方法，并不会启动一个新的线程。
        必须，借助一个线程对象的start()方法，才会启动一个新的线程。
        所以，在创建Thread对象的时候，把battle1作为构造方法的参数传递进去，这个线程启动的时候，就会去执行battle1.run()方法了。*/
