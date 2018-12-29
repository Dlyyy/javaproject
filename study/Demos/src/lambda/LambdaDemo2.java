package lambda;

import character.Hero1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
//匿名类方式
//首先准备一个接口HeroChecker，提供一个test(Hero)方法
//然后通过匿名类的方式，实现这个接口
//接着调用filter，传递这个checker进去进行判断，这种方式就很像通过Collections.sort在对一个Hero集合排序，
// 需要传一个Comparator的匿名类对象进去一样。
public class LambdaDemo1 {
    public static void main(String[] args) {
        Random r = new Random();
        List<Hero1> heros = new ArrayList<Hero1>();
        for (int i = 0; i < 5; i++) {
            heros.add(new Hero1("hero " + i, r.nextInt(1000), r.nextInt(100)));
        }
        System.out.println("初始化后的集合：");
        System.out.println(heros);
        System.out.println("使用匿名类的方式，筛选出 hp>100 && damange<50的英雄");
        HeroChecker checker = new HeroChecker() {
            @Override
            public boolean test(Hero1 h) {
                return (h.hp>100 && h.damage<50);
            }
        };

        filter(heros,checker);
    }

    private static void filter(List<Hero1> heros,HeroChecker checker) {
        for (Hero1 hero : heros) {
            if(checker.test(hero))
                System.out.print(hero);
        }
    }
}