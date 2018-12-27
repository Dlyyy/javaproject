package digit;

public class StringDemo2 {
    public static void main(String[] args) {
        /*str1和str2的内容一定是一样的！
        但是，并不是同一个字符串对象*/
        String str1 = "the light";

        String str2 = new String(str1);

        //==用于判断是否是同一个字符串对象
        System.out.println( str1  ==  str2);   //false

        String str3 = "the light";
        System.out.println( str1  ==  str3);//true 编译器发现已经存在现成的"the light"，那么就直接拿来使用，而没有进行重复创建

        String str4 = new String(str1);

        String str5 = str1.toUpperCase();

        //==用于判断是否是同一个字符串对象
        System.out.println( str1  ==  str4);//false

        System.out.println(str1.equals(str4));//完全一样返回true

        System.out.println(str1.equals(str5));//大小写不一样，返回false
        System.out.println(str1.equalsIgnoreCase(str5));//忽略大小写的比较，返回true

        String start = "the";
        String end = "Ight";

        System.out.println(str1.startsWith(start));//是否以...开始
        System.out.println(str1.endsWith(end));//是否以...结束


    }
}
