import java.util.Scanner;
//关系操作符
public class Operator2 {
    public static void main(String[] args) {
        Scanner s1 = new Scanner(System.in);
        System.out.println("please input the first integer: ");
        int i1 = s1.nextInt();
        System.out.println("please input the second integer: ");
        int i2 = s1.nextInt();
        System.out.println("compare " + i1 + ">" + i2 + ":" + (i1 > i2));
        System.out.println("compare " + i1 + ">=" + i2 + ":" + (i1 >= i2));
        System.out.println("compare " + i1 + "<" + i2 + ":" + (i1 < i2));
        System.out.println("compare " + i1 + "<=" + i2 + ":" + (i1 <= i2));
        System.out.println("compare " + i1 + "==" + i2 + ":" + (i1 == i2));
        System.out.println("compare " + i1 + "!=" + i2 + ":" + (i1 != i2));
    }

}