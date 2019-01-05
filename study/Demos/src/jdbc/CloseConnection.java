package jdbc;
/*数据库的连接是有限资源，相关操作结束后，养成关闭数据库的好习惯
        先关闭Statement
        后关闭Connection*/
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CloseConnection {
    public static void main(String[] args) {
        Connection c = null;
        Statement s = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");

            c = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/how2java?characterEncoding=UTF-8", "root",
                    "admin");

            s = c.createStatement();

            String sql = "insert into hero values(null," + "'提莫'" + "," + 313.0f + "," + 50 + ")";

            s.execute(sql);

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            // 数据库的连接时有限资源，相关操作结束后，养成关闭数据库的好习惯
            // 先关闭Statement
            if (s != null)
                try {
                    s.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            // 后关闭Connection
            if (c != null)
                try {
                    c.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

        }
    }
}
