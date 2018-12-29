package lambda;

import character.Hero1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
//不用Lambda，普通方法
public class LambdaDemo1 {
    public static void main(String[] args) {
        Random r = new Random();
        List<Hero1> heros = new ArrayList<Hero1>();
        for (int i = 0; i < 10; i++) {
            heros.add(new Hero1("hero " + i, r.nextInt(1000), r.nextInt(100)));
        }
        System.out.println("初始化后的集合：");
        System.out.println(heros);
        System.out.println("筛选出 hp>100 && damange<50的英雄");
        filter(heros);
    }

    private static void filter(List<Hero1> heros) {
        for (Hero1 hero : heros) {
            if(hero.hp>100 && hero.damage<50)
                System.out.print(hero);
        }
    }
}
