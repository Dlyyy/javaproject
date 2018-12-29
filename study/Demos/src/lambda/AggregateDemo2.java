package lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import character.Hero1;

public class AggregateDemo2 {
    public static void main(String[] args) {
        Random r = new Random();
        List<Hero1> heros = new ArrayList<Hero1>();
        for (int i = 0; i < 5; i++) {
            heros.add(new Hero1("hero " + i, r.nextInt(1000), r.nextInt(100)));
        }
        //管道源是集合
        heros
                .stream()
                .forEach(h->System.out.println(h.name));

        //管道源是数组
        Hero1 hs[] = heros.toArray(new Hero1[heros.size()]);
        Arrays.stream(hs)
                .forEach(h->System.out.println(h.name));

    }
}

/*把Collection切换成管道源很简单，调用stream()就行了。

        heros.stream()


        但是数组却没有stream()方法，需要使用

        Arrays.stream(hs)


        或者

        Stream.of(hs)*/
