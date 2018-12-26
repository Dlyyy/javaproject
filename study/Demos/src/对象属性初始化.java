public class 对象属性初始化 {

    //    对象属性初始化有3种
    //1. 声明该属性的时候初始化
    //2. 构造方法中初始化
    //3. 初始化块

    public String name = "some hero"; //声明该属性的时候初始化
    protected float hp;
    float maxHP;

    {
        maxHP = 200; //初始化块
    }

    public Hero(){
        hp = 100; //构造方法中初始化

    }
}
