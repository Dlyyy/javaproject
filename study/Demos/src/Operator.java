import java.util.Scanner;

public class Operator {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int a = s.nextInt();
        System.out.println("第一个整数："+a);
        int b = s.nextInt();
        System.out.println("第一个整数："+b);
        String rn = s.nextLine();
        //需要注意的是，如果在通过nextInt()读取了整数后，再接着读取字符串，读出来的是回车换行:"\r\n",因为nextInt仅仅读取数字信息，而不会读取回车换行"\r\n".
        //所以，如果在业务上需要读取了整数后，接着读取字符串，那么就应该连续执行两次nextLine()，第一次是取走回车换行，第二次才是读取真正的字符串
        String c = s.nextLine();
        System.out.println("读取的字符串是："+c);

        //如果有任何运算单元的长度超过int,那么运算结果就按照最长的长度计算
        //如果任何运算单元的长度都不超过int,那么运算结果就按照int来计算
        //byte a = 1;
        //byte b= 2;
        //a+b -> int 类型
        int a1 = 5;
        long b1 = 6;
        int c1 = (int) (a1+b1); //a+b的运算结果是long型，所以要进行强制转换

        //i++; 先取值，再运算
        //++i; 先运算，再取值
        int i = 5;
        System.out.println(i++); //输出5
        System.out.println(i);   //输出6

        int j = 5;
        System.out.println(++j); //输出6
        System.out.println(j);   //输出6

        //三元操作符
//        表达式?值1:值2
//        如果表达式为真 返回值1
//        如果表达式为假 返回值2
        //        int i = 5;
        //        int j = 6;
        //
        //        int k = i < j ? 99 : 88;
        //
        //        // 相当于
        //        if (i < j) {
        //            k = 99;
        //        } else {
        //            k = 88;
        //        }
    }
}
