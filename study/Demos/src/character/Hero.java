package character;

public class Hero {
    public String name;
    public float hp;

    public int damage;
    public int id;

    public Hero() {

    }

    // 增加一个初始化name的构造方法
    public Hero(String name) {

        this.name = name;
    }

    // 重写toString方法
    public String toString() {
        return name;
    }
}
