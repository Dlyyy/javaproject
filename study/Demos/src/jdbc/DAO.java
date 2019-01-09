package jdbc;

import java.util.List;

import character.Hero;

/*DAO=DataAccess Object

数据库访问对象

实际上就是运用了练习-ORM中的思路，把数据库相关的操作都封装在这个类里面，其他地方看不到JDBC的代码*/

public interface DAO {

    //增加
    public void add(Hero hero);
    //修改
    public void update(Hero hero);
    //删除
    public void delete(int id);
    //获取
    public Hero get(int id);
    //查询
    public List<Hero> list();
    //分页查询
    public List<Hero> list(int start, int count);
}
