import java.util.Date;

public class DateDemo {
    public static void main(String[] args) {
        Date d1 =new Date();
        System.out.println("当前时间:");
        System.out.println(d1);
        System.out.println();
        // 从1970年1月1日 早上8点0分0秒 开始经历的毫秒数
        Date d2 = new Date(5000);
        System.out.println("从1970年1月1日 早上8点0分0秒 开始经历了5秒的时间");
        System.out.println(d2);

        //注意：是java.util.Date;
        //而非 java.sql.Date，此类是给数据库访问的时候使用的
        Date now= new Date();
        //打印当前时间
        System.out.println("当前时间:"+now.toString());
        //getTime() 得到一个long型的整数
        //这个整数代表 1970.1.1 08:00:00:000，每经历一毫秒，增加1
        System.out.println("当前时间getTime()返回的值是："+now.getTime());

        Date zero = new Date(0);
        System.out.println("用0作为构造方法，得到的日期是:"+zero);

        //当前日期的毫秒数
        System.out.println("Date.getTime() \t\t\t返回值: "+now.getTime());
        //通过System.currentTimeMillis()获取当前日期的毫秒数
        System.out.println("System.currentTimeMillis() \t返回值: "+System.currentTimeMillis());
    }
}
