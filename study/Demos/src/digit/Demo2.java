package digit;

public class Demo2 {
    public static void main(String[] args) {
        /*        数字转字符串
        方法1： 使用String类的静态方法valueOf
        方法2： 先把基本类型装箱为对象，然后调用对象的toString*/
        int i = 5;

        //方法1
        String str = String.valueOf(i);

        //方法2
        Integer it = i;
        String str2 = it.toString();

/*        字符串转数字
                调用Integer的静态方法parseInt*/
        String str1 = "999";

        int i1= Integer.parseInt(str1);

        System.out.println(i1);

    }
}
