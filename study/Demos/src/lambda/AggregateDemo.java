package lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import character.Hero1;

public class AggregateDemo {

    public static void main(String[] args) {
        Random r = new Random();
        List<Hero1> heros = new ArrayList<Hero1>();
        for (int i = 0; i < 5; i++) {
            heros.add(new Hero1("hero " + i, r.nextInt(1000), r.nextInt(100)));
        }

        System.out.println("初始化后的集合：");
        System.out.println(heros);
        System.out.println("查询条件：hp>100 && damage<50");
        System.out.println("通过传统操作方式找出满足条件的数据：");

        for (Hero1 h : heros) {
            if (h.hp > 100 && h.damage < 50)
                System.out.println(h.name);
        }

        System.out.println("通过聚合操作方式找出满足条件的数据：");
        heros
                .stream()
                .filter(h -> h.hp > 100 && h.damage < 50)
                .forEach(h -> System.out.println(h.name));

    }
}
/*
要了解聚合操作，首先要建立Stream和管道的概念
        Stream 和Collection结构化的数据不一样，Stream是一系列的元素，就像是生产线上的罐头一样，一串串的出来。
        管道指的是一系列的聚合操作。

        管道又分3个部分
        管道源：在这个例子里，源是一个List
        中间操作： 每个中间操作，又会返回一个Stream，比如.filter()又返回一个Stream, 中间操作是“懒”操作，并不会真正进行遍历。
        结束操作：当这个操作执行后，流就被使用“光”了，无法再被操作。所以这必定是流的最后一个操作。
        结束操作不会返回Stream，但是会返回int、float、String、 Collection或者像forEach，什么都不返回,
        结束操作才进行真正的遍历行为，在遍历的时候，才会去进行中间操作的相关判断

        注： 这个Stream和I/O章节的InputStream,OutputStream是不一样的概念。*/
