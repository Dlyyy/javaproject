package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class 使用事务 {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection c = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/how2java?characterEncoding=UTF-8","root", "admin");
             Statement s = c.createStatement();) {

            // 有事务的前提下
            // 在事务中的多个操作，要么都成功，要么都失败

            c.setAutoCommit(false);

            // 加血的SQL
            String sql1 = "update hero set hp = hp +1 where id = 22";
            s.execute(sql1);

            // 减血的SQL
            // 不小心写错写成了 updata(而非update)

            String sql2 = "updata hero set hp = hp -1 where id = 22";
            s.execute(sql2);

            // 手动提交
            c.commit();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
/*
在事务中的多个操作，要么都成功，要么都失败
通过 c.setAutoCommit(false);关闭自动提交
使用 c.commit();进行手动提交
在22行-35行之间的数据库操作，就处于同一个事务当中，要么都成功，要么都失败
所以，虽然第一条SQL语句是可以执行的，但是第二条SQL语句有错误，其结果就是两条SQL语句都没有被提交。 除非两条SQL语句都是正确的。*/
