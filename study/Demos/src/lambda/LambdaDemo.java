package lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import character.Hero1;

public class LambdaDemo {
    public static void main(String[] args) {
        Random r = new Random();
        List<Hero1> heros = new ArrayList<Hero1>();
        for (int i = 0; i < 5; i++) {
            heros.add(new Hero1("hero " + i, r.nextInt(1000), r.nextInt(100)));
        }
        System.out.println("初始化后的集合：");
        System.out.println(heros);
        System.out.println("使用Lamdba的方式，筛选出 hp>100 && damange<50的英雄");
        filter(heros,h->h.hp>100 && h.damage<50);
    }

    private static void filter(List<Hero1> heros,HeroChecker checker) {
        for (Hero1 hero : heros) {
            if(checker.test(hero))
                System.out.print(hero);
        }
    }
}
/*
    使用Lambda方式筛选出数据
    filter(heros,(h)->h.hp>100 && h.damage<50);
    同样是调用filter方法，从上一步的传递匿名类对象，变成了传递一个Lambda表达式进去
    h->h.hp>100 && h.damage<50
    咋一看Lambda表达式似乎不好理解，其实很简单，下一步讲解如何从一个匿名类一点点演变成Lambda表达式

    Lambda表达式虽然带来了代码的简洁，但是也有其局限性。
1. 可读性差，与啰嗦的但是清晰的匿名类代码结构比较起来，Lambda表达式一旦变得比较长，就难以理解
2. 不便于调试，很难在Lambda表达式中增加调试信息，比如日志
3. 版本支持，Lambda表达式在JDK8版本中才开始支持，如果系统使用的是以前的版本，考虑系统的稳定性等原因，而不愿意升级，那么就无法使用。

Lambda比较适合用在简短的业务代码中，并不适合用在复杂的系统中，会加大维护成本。*/
