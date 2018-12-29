package lambda;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import character.Hero1;

public class 引用静态方法 {
    public static void main(String[] args) {
        Random r = new Random();
        List<Hero1> heros = new ArrayList<Hero1>();
        for (int i = 0; i < 5; i++) {
            heros.add(new Hero1("hero " + i, r.nextInt(1000), r.nextInt(100)));
        }
        System.out.println("初始化后的集合：");
        System.out.println(heros);

        HeroChecker c = new HeroChecker() {
            public boolean test(Hero1 h) {
                return h.hp>100 && h.damage<50;
            }
        };

        System.out.println("使用匿名类过滤");
        filter(heros, c);
        System.out.println("使用Lambda表达式");
        filter(heros, h->h.hp>100 && h.damage<50);
        System.out.println("在Lambda表达式中使用静态方法");
        filter(heros, h -> 引用静态方法.testHero1(h) );
        System.out.println("直接引用静态方法");
        filter(heros, 引用静态方法::testHero1);
    }

    public static boolean testHero1(Hero1 h) {
        return h.hp>100 && h.damage<50;
    }

    private static void filter(List<Hero1> heros, HeroChecker checker) {
        for (Hero1 hero : heros) {
            if (checker.test(hero))
                System.out.print(hero);
        }
    }
}
