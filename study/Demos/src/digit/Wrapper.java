package digit;

public class Wrapper {
    public static void main(String[] args) {
        int i =5;
        //把一个基本类型的变量,转换为Integer对象
        Integer it = new Integer(i);
        //把一个Integer对象，转换为一个基本类型的int
        int i1  = it.intValue();
        System.out.println(i1);

        //Integer是Number的子类，所以打印true
        System.out.println(it instanceof Number);

//        不需要调用构造方法，通过=符号自动把 基本类型 转换为 类类型 就叫装箱
        //自动转换就叫装箱
        int i2 = 4;
        Integer it2 = i2;
        System.out.println(it2);

//        不需要调用Integer的intValue方法，通过=就自动转换成int类型，就叫拆箱
        int i3 = 3;
        Integer it3 = new Integer(i3);
        int i4 = it;//自动转换就叫拆箱
        System.out.println(i3);

        //int的最大值可以通过其对应的封装类Integer.MAX_VALUE获取
        //int的最大值
        System.out.println(Integer.MAX_VALUE);
        //int的最小值
        System.out.println(Integer.MIN_VALUE);
    }

}
