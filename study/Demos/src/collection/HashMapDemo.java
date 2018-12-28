package collection;

import character.Hero;

import java.util.HashMap;

//HashMap储存数据的方式是—— 键值对
public class HashMapDemo {
    public static void main(String[] args) {
        HashMap<String,String> dictionary = new HashMap<>();
        dictionary.put("adc", "物理英雄");
        dictionary.put("apc", "魔法英雄");
        dictionary.put("t", "坦克");

        System.out.println(dictionary.get("t"));


        /*键不能重复，值可以重复
        对于HashMap而言，key是唯一的，不可以重复的。
        所以，以相同的key 把不同的value插入到 Map中会导致旧元素被覆盖，只留下最后插入的元素。
        不过，同一个对象可以作为值插入到map中，只要对应的key不一样*/
        HashMap<String, Hero> heroMap = new HashMap<String,Hero>();

        heroMap.put("gareen", new Hero("gareen1"));
        System.out.println(heroMap);

        //key为gareen已经有value了，再以gareen作为key放入数据，会导致原英雄，被覆盖
        //不会增加新的元素到Map中
        heroMap.put("gareen", new Hero("gareen2"));
        System.out.println(heroMap);

        //清空map
        heroMap.clear();
        Hero gareen = new Hero("gareen");

        //同一个对象可以作为值插入到map中，只要对应的key不一样
        heroMap.put("hero1", gareen);
        heroMap.put("hero2", gareen);

        System.out.println(heroMap);
    }
}
