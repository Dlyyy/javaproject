package lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import character.Hero1;

public class 引用容器中的对象的方法 {
    public static void main(String[] args) {
        Random r = new Random();
        List<Hero1> heros = new ArrayList<Hero1>();
        for (int i = 0; i < 5; i++) {
            heros.add(new Hero1("hero " + i, r.nextInt(1000), r.nextInt(100)));
        }
        System.out.println("初始化后的集合：");
        System.out.println(heros);

        System.out.println("Lambda表达式：");
        filter(heros,h-> h.hp>100 && h.damage<50 );

        System.out.println("Lambda表达式中调用容器中的对象的matched方法：");
        filter(heros,h-> h.matched() );

        System.out.println("引用容器中对象的方法 之过滤结果：");
        filter(heros, Hero1::matched);
    }

    public boolean testHero(Hero1 h) {
        return h.hp>100 && h.damage<50;
    }

    private static void filter(List<Hero1> heros, HeroChecker checker) {
        for (Hero1 hero : heros) {
            if (checker.test(hero))
                System.out.print(hero);
        }
    }

}