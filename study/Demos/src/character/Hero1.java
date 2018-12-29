package character;

public class Hero1 implements Comparable<Hero1> {
    public String name;
    public float hp;

    public int damage;

    public Hero1(){

    }

    public Hero1(String name) {
        this.name =name;

    }

    //初始化name,hp,damage的构造方法
    public Hero1(String name,float hp, int damage) {
        this.name =name;
        this.hp = hp;
        this.damage = damage;
    }

    @Override
    public int compareTo(Hero1 anotherHero1) {
        if(damage<anotherHero1.damage)
            return 1;
        else
            return -1;
    }

    @Override
    public String toString() {
        return "Hero1 [name=" + name + ", hp=" + hp + ", damage=" + damage + "]\r\n";
    }
}
