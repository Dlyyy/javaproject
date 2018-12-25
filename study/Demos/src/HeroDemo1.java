public class HeroDemo1 {
    public static void main(String[] args) {
        Hero garen = new Hero();
        garen.name = "盖伦";
        garen.hp = 616.28f;
        garen.armor = 27.536f;
        garen.moveSpeed = 350;
        garen.addSpeed(50);
        garen.keng();


        Hero teemo = new Hero();
        teemo.name = "提莫";
        teemo.hp = 383f;
        teemo.armor = 14f;
        teemo.moveSpeed = 330;
        teemo.keng();
    }

}
class Hero {      //不要把Hero类放在HeroDemo类中   否则会出现错误：无法从静态上下文中引用非静态 变量 this  因为静态方法中不能引用非静态变量

    String name; //姓名

    float hp; //血量

    float armor; //护甲

    int moveSpeed; //移速

    void keng() {
        System.out.println("keng dui you");
    }

    public float getHp() {
        return hp;
    }

    public float getArmor() {
        return armor;
    }

    void addSpeed(int speed) {
        moveSpeed = moveSpeed + speed;
    }
}


