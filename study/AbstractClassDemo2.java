class AbstractClassDemo2 {
	public static void main(String[] args) 	{
		Cat cat = new Cat();
		cat.cry();
	}
}
//抽象类
abstract class Animal{
	public String name ;
	//抽象方法
	public abstract void cry();
	public Animal(){
		System.out.println("new Animal()");
	}
	public Animal(String fenbei){
		System.out.println("new Animal("+fenbei+")");
	}
}
//具体类
class Cat extends Animal{
	public Cat(){
		super("20");
	}
	public void cry(){
		System.out.println("喵喵~~~");
	}
}