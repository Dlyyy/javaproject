package collection;

import character.Hero;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
//3种遍历方法
public class TraversalDemo {
    public static void main(String[] args) {
        List<Hero> heros = new ArrayList<Hero>();

        // 放5个Hero进入容器
        for (int i = 0; i < 5; i++) {
            heros.add(new Hero("hero name " + i));
        }

        // 第一种遍历 for循环
        System.out.println("--------for 循环-------");
        for (int i = 0; i < heros.size(); i++) {
            Hero h = heros.get(i);
            System.out.println(h);
        }


        //第二种遍历，使用迭代器
        System.out.println("--------使用while的iterator-------");
        Iterator<Hero> it= heros.iterator();
        //从最开始的位置判断"下一个"位置是否有数据
        //如果有就通过next取出来，并且把指针向下移动
        //直到"下一个"位置没有数据
        while(it.hasNext()){
            Hero h = it.next();
            System.out.println(h);
        }
        //迭代器的for写法
        System.out.println("--------使用for的iterator-------");
        for (Iterator<Hero> iterator = heros.iterator(); iterator.hasNext();) {
            Hero hero = (Hero) iterator.next();
            System.out.println(hero);
        }

        // 第三种，增强型for循环
        /*使用增强型for循环可以非常方便的遍历ArrayList中的元素，这是很多开发人员的首选。

        不过增强型for循环也有不足：
        无法用来进行ArrayList的初始化
        无法得知当前是第几个元素了，当需要只打印单数元素的时候，就做不到了。 必须再自定下标变量。*/

        System.out.println("--------增强型for循环-------");
        for (Hero h : heros) {
            System.out.println(h);
        }

    }
}
