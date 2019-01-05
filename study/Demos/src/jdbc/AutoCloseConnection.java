package jdbc;
/*数据库的连接是有限资源，相关操作结束后，养成关闭数据库的好习惯
        先关闭Statement
        后关闭Connection*/
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

//如果觉得上一步的关闭连接的方式很麻烦，可以参考关闭流 的方式，使用try-with-resource的方式自动关闭连接，
// 因为Connection和Statement都实现了AutoCloseable接口
/*File f = new File("d:/lol.txt");

        //把流定义在try()里,try,catch或者finally结束的时候，会自动关闭
        try (FileInputStream fis = new FileInputStream(f)) {
        byte[] all = new byte[(int) f.length()];
        fis.read(all);
        for (byte b : all) {
        System.out.println(b);
        }
        } catch (IOException e) {
        e.printStackTrace();
        }*/

public class AutoCloseConnection {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (
                Connection c = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/how2java?characterEncoding=UTF-8",
                        "root", "admin");
                Statement s = c.createStatement();
        )
        {
            String sql = "insert into hero values(null," + "'提莫'" + "," + 313.0f + "," + 50 + ")";
            s.execute(sql);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
