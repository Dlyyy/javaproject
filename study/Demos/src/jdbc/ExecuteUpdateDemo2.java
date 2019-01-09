package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*不同1：
execute可以执行查询语句
然后通过getResultSet，把结果集取出来
executeUpdate不能执行查询语句
不同2:
execute返回boolean类型，true表示执行的是查询语句，false表示执行的是insert,delete,update等等
executeUpdate返回的是int，表示有多少条数据受到了影响*/

public class ExecuteUpdateDemo2 {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection c = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/how2java?characterEncoding=UTF-8","root", "admin");
             Statement s = c.createStatement();) {

            // 不同1：execute可以执行查询语句
            // 然后通过getResultSet，把结果集取出来
            String sqlSelect = "select * from hero";

            s.execute(sqlSelect);
            ResultSet rs = s.getResultSet();
            while (rs.next()) {
                System.out.println(rs.getInt("id"));
            }

            // executeUpdate不能执行查询语句
            // s.executeUpdate(sqlSelect);

            // 不同2:
            // execute返回boolean类型，true表示执行的是查询语句，false表示执行的是insert,delete,update等等
            boolean isSelect = s.execute(sqlSelect);
            System.out.println(isSelect);

            // executeUpdate返回的是int，表示有多少条数据受到了影响
            String sqlUpdate = "update Hero set hp = 300 where id < 100";
            int number = s.executeUpdate(sqlUpdate);
            System.out.println(number);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
