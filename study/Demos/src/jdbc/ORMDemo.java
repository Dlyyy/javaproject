package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import character.Hero;

/*
ORM=Object Relationship Database Mapping

        对象和关系数据库的映射

        简单说，一个对象，对应数据库里的一条记录

根据id返回一个Hero对象
提供方法get(int id)
返回一个Hero对象
*/

public class ORMDemo {
    public static Hero get(int id) {
        Hero hero = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection c = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/how2java?characterEncoding=UTF-8","root", "admin");
             Statement s = c.createStatement();) {

            String sql = "select * from hero where id = " + id;

            ResultSet rs = s.executeQuery(sql);

            // 因为id是唯一的，ResultSet最多只能有一条记录
            // 所以使用if代替while
            if (rs.next()) {
                hero = new Hero();
                String name = rs.getString(2);
                float hp = rs.getFloat("hp");
                int damage = rs.getInt(4);
                hero.name = name;
                hero.hp = hp;
                hero.damage = damage;
                hero.id = id;
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return hero;

    }

    public static void main(String[] args) {

        Hero h = get(22);
        System.out.println(h.name);

    }

}
