/**
 * 多态
 */
class MultiStateDemo1 {
	public static void main(String[] args) 	{
		Jing8 jing8 = new Jing8(); //Jing8 jing8 变量类型+变量名称
		jing8.meng();
		//多态
		Dog dog = jing8 ;
		dog.watch();
		//多态
		Animal a = dog ;
		a.cry();
		//多态
		Pet pet = (Pet)a ;
		
		//
		Animal	aa = (Animal)pet ;
		Dog		dd = (Dog)pet ;
		Jing8	cc = (Jing8)pet ;

		Animal aaa = jing8 ;
		Dog bbb = jing8 ;
		Jing8 ccc  = jing8 ;
		Pet ddd = jing8 ;

	}
}
abstract class Animal{
	public void cry(){
		System.out.println("汪汪~~~");
	}
}
class Dog extends Animal{
	public void watch(){
		System.out.println("来人了~~~");
	}
}
class Jing8 extends Dog implements Pet{
	public void meng(){
		System.out.println("!@!@!@");
	}
}
interface Pet{
	void meng();
}