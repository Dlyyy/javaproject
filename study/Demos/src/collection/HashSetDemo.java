package collection;

import java.util.HashSet;
import java.util.Iterator;

//Set中的元素，不能重复
public class HashSetDemo {
    public static void main(String[] args) {

        HashSet<String> names = new HashSet<String>();

        names.add("gareen");

        System.out.println(names);

        //第二次插入同样的数据，是插不进去的，容器中只会保留一个
//        names.add("gareen");
//        System.out.println(names);

        /*Set中的元素，没有顺序。
        严格的说，是没有按照元素的插入顺序排列

        HashSet的具体顺序，既不是按照插入顺序，也不是按照hashcode的顺序。关于hashcode有专门的章节讲解: hashcode 原理。

        不保证Set的迭代顺序; 确切的说，在不同条件下，元素的顺序都有可能不一样

        换句话说，同样是插入0-9到HashSet中， 在JVM的不同版本中，看到的顺序都是不一样的。 所以在开发的时候，不能依赖于某种臆测的顺序，这个顺序本身是不稳定的*/

        HashSet<Integer> numbers = new HashSet<Integer>();
        numbers.add(9);
        numbers.add(5);
        numbers.add(1);
        // Set中的元素排列，不是按照插入顺序
        System.out.println(numbers);


        /*Set不提供get()来获取指定位置的元素
        所以遍历需要用到迭代器，或者增强型for循环*/
        HashSet<Integer> numbers1 = new HashSet<Integer>();

        for (int i = 0; i < 20; i++) {
            numbers1.add(i);
        }

        //Set不提供get方法来获取指定位置的元素
        //numbers.get(0)

        //遍历Set可以采用迭代器iterator
        for (Iterator<Integer> iterator = numbers1.iterator(); iterator.hasNext();) {
            Integer i = (Integer) iterator.next();
            System.out.println(i);
        }

        //或者采用增强型for循环
        for (Integer i : numbers1) {
            System.out.println(i);
        }
    }
}
