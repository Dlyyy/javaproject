class AbstractClassDemo2 {
	public static void main(String[] args) 	{
		Cat cat = new Cat();
		cat.cry();
	}
}
//������
abstract class Animal{
	public String name ;
	//���󷽷�
	public abstract void cry();
	public Animal(){
		System.out.println("new Animal()");
	}
	public Animal(String fenbei){
		System.out.println("new Animal("+fenbei+")");
	}
}
//������
class Cat extends Animal{
	public Cat(){
		super("20");
	}
	public void cry(){
		System.out.println("����~~~");
	}
}